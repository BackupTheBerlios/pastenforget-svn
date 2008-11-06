package download.irc;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import download.Status;
import exception.CancelException;
import exception.StopException;

public class DCCDownload implements Runnable {
	private final IRC irc;
	private final DCCPackage dccPackage;
	private final BlockingQueue<String> eventQueue;
	
	public DCCDownload(IRC irc, DCCPackage dccPackage, BlockingQueue<String> eventQueue) {
		this.irc = irc;
		this.dccPackage = dccPackage;
		this.eventQueue = eventQueue;
	}

	@Override
	public void run() {
		this.irc.setStatus(Status.getActive());
		RandomAccessFile randomAccessFile = null;
		InputStream sockStream = null;
		Socket socket = null;
		try {
			String filename = irc.getFileName();
			String ip = irc.getDownloadIp();
			Integer port = dccPackage.getPort();
			Long filesize = dccPackage.getFileSize();
			System.out.println(filesize + " Bytes");

			File file = new File(filename);
			randomAccessFile = new RandomAccessFile(file, "rw");
			randomAccessFile.seek(dccPackage.getDownloadedFileSize());
			socket = new Socket(ip, port);
			sockStream = socket.getInputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = sockStream.read(buffer)) > -1) {
				if(irc.isStopped()) {
					throw new StopException();
				}
				if(irc.isCanceled()) {
					throw new CancelException();
				}
				irc.setCurrentSize(irc.getCurrentSize() + len);
				randomAccessFile.write(buffer, 0, len);
			}
			
			file = new File(filename);
			if (file.length() < dccPackage.getFileSize()) {
				this.eventQueue.put("resume current download");
				System.out.println("Download unvollständig:");
				System.out.println("Geladen: " + file.length());
				System.out.println("Noch zu laden: "
						+ (dccPackage.getFileSize() - file.length()));
				System.out.println("Gesamtgroeße: " + dccPackage.getFileSize());
			}
			
			irc.getQueue().downloadFinished(irc);
		} catch (InterruptedException ie) {
			System.out.println("DCCDownload abgebrochen");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (StopException se) {
			
		} catch (CancelException ce) {
			
		} finally {
			try {
				if (randomAccessFile != null) {
					randomAccessFile.close();
				}
				if (sockStream != null) {
					sockStream.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
