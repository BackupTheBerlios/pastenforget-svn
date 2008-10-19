package download.hoster.streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import middleware.Tools;
import queue.Queue;

public class RedTube extends Stream {

	public RedTube(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setFileName(this.createFileName());
		this.setHosterCaption("redtube");
	}

	@Override
	public String createFileName() {
		try {
			URL url = this.getUrl();
			InputStream is = url.openConnection().getInputStream();
			String title = Tools.createTitleFromWebSource(is);
			String fileName = title.replace("RedTube - ", "") + ".flv";
			return Tools.createWellFormattedFileName(fileName);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new String("unknown");
		}
	}
}
