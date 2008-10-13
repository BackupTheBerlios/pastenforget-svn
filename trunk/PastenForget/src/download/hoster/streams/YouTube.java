package download.hoster.streams;

import java.io.File;
import java.io.InputStream;
import java.net.URL;


import parser.Parser;
import queue.Queue;

public class YouTube extends Stream {

	public YouTube(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.getUrl().toString());
		this.setHosterCaption("youtube");
	}

	@Override
	public String createFileName() {
		try {
			URL url = this.getUrl();
			InputStream is = url.openConnection().getInputStream();
			this.setStatus("ermittle Dateiname");
			String page = Parser.convertStreamToString(is, false);

			String title = Parser.getComplexTag("title", page).get(0);
			String filename = Parser.getTagContent("title", title).replace(
					"YouTube - ", "").replaceAll("&[^;]+;", "").replace("/",
					"-")
					+ ".flv";
			this.setStatus("ermittle Direktlink");
			return filename;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String("youtubeSample.flv");
	}

}
