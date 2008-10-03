package stream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import download.Download;

/**
 * Laedt eine Datei gezielt von einem Webserver.
 * 
 * @author cschaedl
 */

public class ServerDownload {

	/**
	 * RAM-zu-HDD-Thread
	 */
	private class BufferedWriter extends Thread {
		private OutputStream os = null;
		private final BufferSingle buf;

		public BufferedWriter(BufferSingle buf, File file) {
			this.buf = buf;
			try {
				this.os = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			Packet packet = null;
			try {
				do {
					packet = buf.read();
					os.write(packet.getBuffer(), 0, packet.getReceivedBytes());
				} while (!buf.isComplete());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Download download;
	protected ServerConnection connection;

	public ServerDownload(Download download) throws Exception {
		this.download = download;
	}

	public void download() {
		try {
			Long targetFilesize;
			int receivedBytes;
			
			this.connection = new ServerConnection(download.getDirectUrl());
			BufferedInputStream is = new BufferedInputStream(this.connection
					.openDownloadStream());
			BufferSingle buf = new BufferSingle(is);
			File destination = this.download.getDestination();
			String filename;
			if(destination == null) {
				filename = this.download.getFileName();
			} else {
				filename = destination.getPath() +  File.separator +this.download.getFileName();
			}
			
			OutputStream os = new FileOutputStream(filename);

			
			targetFilesize = Long.valueOf(this.connection.getHeader().get(
					"Content-Length").get(0));
			this.download.setFileSize(targetFilesize);
			this.download.setStatus("aktiv");
			Packet packet = null;
			/*
			 * RAM-zu-HDD-Thread; BufferedWriter writer = new
			 * BufferedWriter(buf, new File(this.download.getFileName()));
			 * writer.start();
			 */

			while ((receivedBytes = buf.write()) > 0) {
				packet = buf.read();
				os.write(packet.getBuffer(), 0, packet.getReceivedBytes());
				this.download.setCurrentSize(this.download.getCurrentSize()
						+ receivedBytes);
			}
			buf.setComplete();

			/*
			 * Download wartet auf RAM-zu-HDD-Thread; try { writer.join(); }
			 * catch (InterruptedException e) { System.out.println("Thread
			 * interrupted"); e.printStackTrace(); }
			 */

			os.close();
			is.close();
			this.connection.close();

			if (this.download == this.download.getQueue().getCurrent()) {
				this.download.getQueue().removeCurrent();
			}

		} catch (MalformedURLException muex) {
			System.out.println("Die URL ist nicht normgerecht");
		} catch (IOException ioex) {
			System.err.println(ioex.getMessage());
			ioex.printStackTrace();
		}
	}
}