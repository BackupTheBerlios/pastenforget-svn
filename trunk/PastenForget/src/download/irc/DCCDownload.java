package download.irc;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import download.Download;
import download.Status;
import exception.CancelException;
import exception.StopException;

public class DCCDownload implements Runnable {
	private final Download download;
	private final DCCPackage dccPackage;
	private final BlockingQueue<String> eventQueue;
	
	public DCCDownload(DCCPackage dccPackage, BlockingQueue<String> eventQueue, Download download) {
		this.dccPackage = dccPackage;
		this.eventQueue = eventQueue;
		this.download = download;
	}

	@Override
	public void run() {
		this.download.setStatus(Status.getActive());
		RandomAccessFile randomAccessFile = null;
		InputStream sockStream = null;
		Socket socket = null;
		try {
			String filename = dccPackage.getFileName();
			String ip = dccPackage.getIP();
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
				if(download.isStopped()) {
					throw new StopException();
				}
				if(download.isCanceled()) {
					throw new CancelException();
				}
				download.setCurrentSize(download.getCurrentSize() + len);
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
