package download.hoster;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import parser.Parser;
import queue.Queue;
import stream.ServerDownload;
import download.Download;
import exception.CancelException;
import exception.StopException;

public class RedTube extends Download {

	public RedTube(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.getUrl().toString());
	}

	public String createFilename() {
		try {
			URL url = this.getUrl();
			InputStream is = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(is, false);
			String title = Parser.getComplexTag("title", page).get(0);
			String fileName = Parser.getTagContent("title", title).replace(
					"RedTube - ", "");
			String fileNameExtension = ".flv";
			String parsedFileName = Parser.parseFileName(fileName) + fileNameExtension;
			System.out.println(parsedFileName);
			return parsedFileName;
		} catch (Exception e) {
			e.printStackTrace();
			return new String("unknown");
		}
	}

	@Override
	public void run() {
		try {
			this.setStatus("ermittle Dateiname");
			String filename = this.createFilename();
			this.setFileName(filename);
			this.setStatus("ermittle Directlink");
			String action = "http://www.redtubedownload.info/index.php?"
					+ "action=get_movie&ajax=true&url="
					+ URLEncoder.encode(this.getUrl().toString(), "UTF-8");
			URL url = new URL(action);
			InputStream is = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(is, false);
			List<String> links = Parser.getSimpleTag("a", page);
			String targetLink = new String();
			for (String link : links) {
				if (link.indexOf("redtube") != -1) {
					targetLink = link;
				}
			}
			if (targetLink == null) {
				this.run();
			}
			System.out.println(Parser.getAttribute("href", targetLink));
			this.setDirectUrl(new URL(Parser.getAttribute("href", targetLink)));
			this.isCanceled();
			this.isStopped();
			ServerDownload.download(this);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (StopException se) {
			System.out.println("Download stopped: " + this.getFileName());
		} catch (CancelException ce) {
			System.out.println("Download canceled: " + this.getFileName());
		}
	}

}
