package download.hoster;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import parser.Parser;
import queue.Queue;
import stream.ServerDownload;
import download.Download;
import download.DownloadInterface;

public class Uploaded extends Download implements DownloadInterface {
	private int counter = 0;

	public Uploaded(URL url, Queue queue) {
		this.setUrl(url);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(url.toString());
	}

	@Override
	public void run() {
		try {
			URL url = this.getUrl();
			InputStream in = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(in, true);
			if (page.indexOf("Your Free-Traffic is exceeded!") != -1) {
				this.setStatus("No Free Traffic - Versuch: " + ++counter);
				Thread.sleep(600000);
				this.run();
			}

			String form = Parser.getSimpleTag("form", page).get(0);
			String action = Parser.getAttribute("action", form);
			this.setDirectUrl(new URL(action));

			String fileName = Parser.getAttributeLessTag("title", page).get(0)
					.replaceAll("<title>", "");
			fileName = fileName.substring(0, fileName.indexOf(" ..."));
			System.out.println(fileName);

			this.setFileName(fileName);

			this.serverDownload = new ServerDownload(this);
			this.serverDownload.download();

		} catch (IOException ioe) {
			System.out.println("Uploaded.to Seite nicht erreichbar");
		} catch (Exception e) {

		}

	}
}