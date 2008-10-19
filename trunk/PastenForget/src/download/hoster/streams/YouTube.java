package download.hoster.streams;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import middleware.Tools;
import queue.Queue;

public class YouTube extends Stream {

	public YouTube(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.createFileName());
		this.setHosterCaption("youtube");
	}

	@Override
	public String createFileName() {
		try {
			URL url = this.getUrl();
			InputStream is = url.openConnection().getInputStream();
			String title = Tools.createTitleFromWebSource(is);
			String filename = title.replace("YouTube - ", "") + ".flv";
			return Tools.createWellFormattedFileName(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String("youtubeSample.flv");
	}

}
