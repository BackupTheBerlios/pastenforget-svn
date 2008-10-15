package download.hoster.streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import middleware.Tools;
import parser.Tag;
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
			Tag title = Tools.getTitleFromInputStream(is);
			String fileName = title.toString().replace("RedTube - ", "")
					+ ".flv";
			return fileName;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new String("unknown");
		}
	}
}
