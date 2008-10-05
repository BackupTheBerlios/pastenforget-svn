package download.hoster;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

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

			Request request = new Request();

			String requestForm = Parser.getComplexTag("form", page).get(0);
			String action = "http://www.videodl.org"
					+ Parser.getAttribute("action", requestForm);
			request.setAction(action);

			List<String> input = Parser.getSimpleTag("input", requestForm);
			Iterator<String> inputIt = input.iterator();
			while (inputIt.hasNext()) {
				String currentInput = inputIt.next();
				String name = new String();
				String value = new String();
				if ((name = Parser.getAttribute("name", currentInput)) != null) {
					value = Parser.getAttribute("value", currentInput);
					request.addParameter(name, value);
				}
			}
			request.addParameter("searchinput", this.getUrl().toString());
			is = request.request();
			page = Parser.convertStreamToString(is, false);
			String link = Parser.getSimpleTag("a", page).get(0).replace("\\\"",
					"\"");
			this.setDirectUrl(new URL(Parser.getAttribute("href", link)
					.replace(" ", "&nbsp;")));

			this.serverDownload = new ServerDownload(this);
			this.serverDownload.download();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
