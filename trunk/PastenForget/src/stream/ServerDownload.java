package stream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import download.Download;

/**
 * Laedt eine Datei gezielt von einem Webserver.
 * 
 * @author cschaedl
 */

public class ServerDownload {

	public static String checkContentType(Download download) {
		try {
			URLConnection connection = download.getDirectUrl().openConnection();
			Map<String, List<String>> header = connection.getHeaderFields();
			String contentType = header.get("Content-Type").toString();
			return contentType;
		} catch (IOException ie) {
			System.out.println("invalid URL");
		}
		return new String();
	}

	public static void download(Download download) {
		try {
			Long targetFilesize;

			URLConnection connection = download.getDirectUrl().openConnection();
			Map<String, List<String>> header = connection.getHeaderFields();
			BufferedInputStream is = new BufferedInputStream(connection
					.getInputStream());
			BufferSingle buf = new BufferSingle(is);
			File destination = download.getDestination();
			String filename;
			if (destination == null) {
				filename = download.getFileName();
			} else {
				filename = destination.getPath() + File.separator
						+ download.getFileName();
			}

			targetFilesize = Long.valueOf(header.get("Content-Length").get(0));
			String contentType = header.get("Content-Type").toString();
			if (contentType.indexOf("text/html") != -1) {
				System.out.println("Restart");
				connection = null;
				download.run();
			}
			OutputStream os = new FileOutputStream(filename);
			download.setFileSize(targetFilesize);
			download.setStatus("aktiv");
			Packet packet = null;

			int receivedBytes;
			while (download.isAlive() && ((receivedBytes = buf.write()) > 0)) {
				packet = buf.read();
				os.write(packet.getBuffer(), 0, packet.getReceivedBytes());
				download.setCurrentSize(download.getCurrentSize()
						+ receivedBytes);
			}
			buf.setComplete();
			os.close();
			is.close();
			connection = null;

			download.setCurrentSize(0);

			if (download.isAlive()) {
				System.out.println("Download finished: "
						+ download.getFileName());
				if (download == download.getQueue().getCurrent()) {
					download.getQueue().removeCurrent();
				}
			} else if (download.isStopped()) {
				System.out.println("Download stopped: "
						+ download.getFileName());
			} else {
				System.out.println("Download canceled: "
						+ download.getFileName());
				File file = new File(filename);
				if (file.exists()) {
					file.delete();
				}
				if (download == download.getQueue().getCurrent()) {
					download.getQueue().removeCurrent();
				}
			}

		} catch (MalformedURLException me) {
			System.out.println("invalid URL");
			download.stop();
		} catch (FileNotFoundException fe) {
			System.out.println("invalid filename  \"" + download.getFileName()
					+ "\"");
			download.stop();
		} catch (IOException ie) {
			System.out.println("connection interrupted");
			download.stop();
		}
	}
}