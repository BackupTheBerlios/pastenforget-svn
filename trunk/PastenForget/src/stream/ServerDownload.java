package stream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import download.Download;

/**
 * Laedt eine Datei gezielt von einem Webserver.
 * 
 * @author cschaedl
 */

public class ServerDownload {

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
			if (destination == null) {
				filename = this.download.getFileName();
			} else {
				filename = destination.getPath() + File.separator
						+ this.download.getFileName();
			}

			OutputStream os = new FileOutputStream(filename);

			targetFilesize = Long.valueOf(this.connection.getHeader().get(
					"Content-Length").get(0));
			Map<String, List<String>> header = this.connection.getHeader();
			for (String key : header.keySet()) {
				System.out.println(key + " :  " + header.get(key));
			}
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