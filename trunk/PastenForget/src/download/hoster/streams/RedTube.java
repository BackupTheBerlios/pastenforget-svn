package download.hoster.streams;

import java.io.File;
import java.io.InputStream;
import java.net.URL;


import parser.Parser;
import queue.Queue;

public class RedTube extends Stream {

	public RedTube(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.getUrl().toString());
		this.setHosterCaption("redtube");
	}

	@Override
	public String createFileName() {
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

}
