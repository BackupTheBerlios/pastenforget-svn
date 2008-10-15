package download.hoster.streams;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import middleware.Tools;

import parser.Tag;
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
			Tag titleTag = Tools.getTitleFromInputStream(is);
			String filename = titleTag.toString().replace(
					"YouTube - ", "").replaceAll("&[^;]+;", "").replace("/",
					"-")
					+ ".flv";
			return filename;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String("youtubeSample.flv");
	}

}
