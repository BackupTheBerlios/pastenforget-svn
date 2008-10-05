package stream;

import java.io.BufferedInputStream;
import java.io.File;
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
			System.out.println(destination);
			if (destination == null) {
				filename = download.getFileName();
			} else {
				filename = destination.getPath() + File.separator
						+ download.getFileName();
			}

			targetFilesize = Long.valueOf(header.get("Content-Length").get(0));
			String contentType = header.get("Content-Type").toString();
			System.out.println(contentType);
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
			while (((receivedBytes = buf.write()) > 0) && download.isAlive()) {
				packet = buf.read();
				os.write(packet.getBuffer(), 0, packet.getReceivedBytes());
				download.setCurrentSize(download.getCurrentSize()
						+ receivedBytes);
			}
			buf.setComplete();
			os.close();
			is.close();
			connection = null;
			
			if(!download.isAlive()) {
				File file = new File(filename);
				if(file.exists()) {
					file.delete();
				}
			}

			if (download == download.getQueue().getCurrent()) {
				download.getQueue().removeCurrent();
			}

		} catch (MalformedURLException muex) {
			System.out.println("Die URL ist nicht normgerecht");
		} catch (IOException ioex) {
			System.err.println(ioex.getMessage());
			ioex.printStackTrace();
		}
	}
}