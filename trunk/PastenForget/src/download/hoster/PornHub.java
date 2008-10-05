package download.hoster;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import parser.Parser;
import queue.Queue;
import stream.ServerDownload;
import download.Download;

public class PornHub extends Download {
	public PornHub(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.getUrl().toString());
	}

	@Override
	public void run() {
		try {
			URL url = this.getUrl();
			InputStream is = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(is, false);

			String title = Parser.getComplexTag("title", page).get(0);
			String fileName = Parser.getTagContent("title", title).replace(
					" - Pornhub.com", "")
					+ ".flv";
			this.setFileName(fileName);

			String xmlLink = new String();
			for (String param : Parser.getSimpleTag("param", page)) {
				String name = Parser.getAttribute("name", param);
				if ((name != null) && (name.equals("FlashVars"))) {
					xmlLink = Parser.getAttribute("value", param).replace(
							"options=", "");
				}
			}

			url = new URL(xmlLink);
			is = url.openConnection().getInputStream();
			page = Parser.convertStreamToString(is, false);
			String flv = Parser.getComplexTag("flv_url", page).get(0);
			String flvLink = Parser.getTagContent("flv_url", flv);
			this.setDirectUrl(new URL(flvLink));

			this.serverDownload = new ServerDownload(this);
			this.serverDownload.download();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
