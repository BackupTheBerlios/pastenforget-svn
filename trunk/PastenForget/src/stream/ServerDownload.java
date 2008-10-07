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
		BufferedInputStream is = null;
		BufferedOutputStream os = null;
		URLConnection connection = null;
		try {
			Long targetFilesize;

			connection = download.getDirectUrl().openConnection();
			Map<String, List<String>> header = connection.getHeaderFields();
			is = new BufferedInputStream(connection
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
			os = new BufferedOutputStream(new FileOutputStream(filename));
			download.setFileSize(targetFilesize);
			download.setStatus("aktiv");
			
			int receivedBytes;
			byte[] buffer = new byte[2048];
			
			
			while ((receivedBytes = is.read(buffer)) > -1) {
				download.isCanceled();
				download.isStopped();
				os.write(buffer, 0, receivedBytes);
				download.setCurrentSize(download.getCurrentSize()
						+ receivedBytes);
			}
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
			try {
				if(is != null) {
					is.close();
				}
				if(os != null) {
					os.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
			connection = null;
			download.setCurrentSize(0);
		}
	}
}