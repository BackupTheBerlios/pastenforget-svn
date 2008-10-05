package download.hoster;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import parser.Parser;
import parser.Request;
import queue.Queue;
import stream.ServerDownload;
import download.Download;

public class YouTube extends Download {

	public YouTube(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.createFilename());
	}

	public String createFilename() {
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

		return null;
	}

	@Override
	public void run() {
		try {
			URL url = new URL("http://www.videodl.org/");
			InputStream is = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(is, false);

			String requestForm = Parser.getComplexTag("form", page).get(0);
			String action = "http://www.videodl.org"
					+ Parser.getAttribute("action", requestForm);
			Request request = Hoster.readRequestFormular(requestForm);
			request.setAction(action);
			request.addParameter("searchinput", this.getUrl().toString());
			is = request.request();
			page = Parser.convertStreamToString(is, false);
			String link = Parser.getSimpleTag("a", page).get(0).replace("\\\"",
					"\"");
			this.setDirectUrl(new URL(Parser.getAttribute("href", link)
					.replace(" ", "&nbsp;")));

			if (this.isAlive()) {
				ServerDownload.download(this);
			} else {
				if (this.isStopped()) {
					System.out.println("Download stopped: "
							+ this.getFileName());
				} else {
					System.out.println("Download canceled: "
							+ this.getFileName());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
