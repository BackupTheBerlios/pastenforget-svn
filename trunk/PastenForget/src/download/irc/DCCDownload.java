package download.irc;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import download.Download;

public class DCCDownload implements Runnable {
	private final Download download;
	private final DCCPackage dccPackage;
	private final BlockingQueue<String> eventQueue;
	private boolean isInterrupted = false;

	
	public DCCDownload(DCCPackage dccPackage, BlockingQueue<String> eventQueue, Download download) {
		this.dccPackage = dccPackage;
		this.eventQueue = eventQueue;
		this.download = download;
	}

	public synchronized void interrupt() {
		this.isInterrupted = true;
	}

	private synchronized boolean isInterrupted() {
		return this.isInterrupted;
	}

	@Override
	public void run() {
		RandomAccessFile randomAccessFile = null;
		InputStream sockStream = null;
		try {
			String filename = dccPackage.getFileName();
			String ip = dccPackage.getIP();
			Integer port = dccPackage.getPort();
			Long filesize = dccPackage.getFileSize();
			System.out.println(filesize + " Bytes");

			File file = new File(filename);
			randomAccessFile = new RandomAccessFile(file, "rw");
			randomAccessFile.seek(dccPackage.getDownloadedFileSize());
			Socket socket = new Socket(ip, port);
			sockStream = socket.getInputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = sockStream.read(buffer)) > -1) {
				download.setCurrentSize(download.getCurrentSize() + len);
				if (this.isInterrupted()) {
					throw new InterruptedException();
				} else {
					randomAccessFile.write(buffer, 0, len);
				}
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
		} finally {
			try {
				if (randomAccessFile != null) {
					randomAccessFile.close();
				}
				if (sockStream != null) {
					sockStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
