package stream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import download.Download;
import exception.CancelException;
import exception.StopException;

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
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(filename));
			Buffer buf = new Buffer(is, os);
			download.setFileSize(targetFilesize);
			download.setStatus("aktiv");
			
			int receivedBytes;
			while (!buf.isComplete()) {
				receivedBytes = buf.write();
				download.isCanceled();
				download.isStopped();
				buf.write();
				download.setCurrentSize(download.getCurrentSize()
						+ receivedBytes);
			}
			os.close();
			is.close();
			connection = null;

			System.out.println("Download finished: " + download.getFileName());
			if (download == download.getQueue().getCurrent()) {
				download.getQueue().removeCurrent();
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
		} catch (CancelException ce) {
			System.out.println("Download canceled: " + download.getFileName());
			String filename = new String();
			if (download.getDestination() == null) {
				filename = download.getFileName();
			} else {
				filename = download.getDestination().getPath() + File.separator
						+ download.getFileName();
			}
			File file = new File(filename);
			if (file.exists()) {
				file.delete();
			}
			if (download == download.getQueue().getCurrent()) {
				download.getQueue().removeCurrent();
			}
		} catch (StopException se) {
			System.out.println("Download stopped: " + download.getFileName());
		} finally {
			download.setCurrentSize(0);
		}
	}
}