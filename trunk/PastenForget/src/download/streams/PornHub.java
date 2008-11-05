package download.streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import middleware.Tools;
import parser.Tag;
import queue.Queue;
import download.Download;
import exception.CancelException;
import exception.RestartException;
import exception.StopException;
import exception.TagNotSupportedException;

public class PornHub extends Download {

	public PornHub() {
		super();
	}

	public void setInformation(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setFileName(this.createFileName());
	}

	public String createFileName() {
		try {
			URL url = this.getUrl();
			InputStream in = url.openConnection().getInputStream();
			String title = Tools.getTitleFromWebSource(in);
			String fileName = title.replace(" - Pornhub.com", "") + ".flv";
			return Tools.createWellFormattedFileName(fileName);
		} catch (IOException io) {
			return new String("pornhub_"
					+ String.valueOf(System.currentTimeMillis()) + ".flv");
		}
	}

	@Override
	public URLConnection prepareConnection() throws StopException, CancelException, RestartException, IOException {
		URL url = this.getUrl();
		InputStream is = url.openConnection().getInputStream();
		Tag htmlDocument = Tools.createTagFromWebSource(is, false);

		String xmlLink = new String();
		for (Tag param : htmlDocument.getSimpleTag("param")) {
			String name = param.getAttribute("name");
			if ((name != null) && (name.equals("FlashVars"))) {
				xmlLink = param.getAttribute("value").replace("options=", "");
			}
		}

		url = new URL(xmlLink);
		is = url.openConnection().getInputStream();
		htmlDocument = Tools.createTagFromWebSource(is, false);
		Tag flv = htmlDocument.getComplexTag("flv_url").get(0);
		String flvLink = new String();
		try {
			flvLink = flv.getTagContent(false);
		} catch (TagNotSupportedException noValidTag) {
			noValidTag.printStackTrace();
		}
		this.setDirectUrl(new URL(flvLink));

		this.checkStatus();
		URLConnection urlc = this.getDirectUrl().openConnection();
		
		return urlc;
	}

}
