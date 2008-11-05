package download.hoster;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import middleware.Tools;
import parser.Tag;
import queue.Queue;
import download.Download;
import download.DownloadInterface;
import download.Status;
import exception.CancelException;
import exception.RestartException;
import exception.StopException;

public class Uploaded extends Download implements DownloadInterface {

	private int counter = 0;

	public Uploaded() {
		super();
	}

	public void setInformation(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus(Status.getActive());
		this.setFileName(this.createFileName());
	}

	public String createFileName() {
		try {
			URL url = this.getUrl();
			InputStream in = url.openConnection().getInputStream();
			String title = Tools.getTitleFromWebSource(in);
			String fileName = title.substring(0, title.indexOf(" ..."));
			return Tools.createWellFormattedFileName(fileName);
		} catch (IOException ioError) {
			return this.getUrl().toString();
		}
	}

	@Override
	public URLConnection prepareConnection() throws StopException, CancelException, RestartException, IOException {
		URL url = this.getUrl();
		InputStream in = url.openConnection().getInputStream();
		Tag htmlDocument = Tools.createTagFromWebSource(in, false);
		if (htmlDocument.toString().indexOf("Your Free-Traffic is exceeded!") != -1) {
			this.setStatus("No Free Traffic - Versuch: " + ++counter);
			try {
				System.out.println("Error: Download limit exceeded");
				for(int i = 0; i < 600; i++) {
					Thread.sleep(1000);
					this.checkStatus();
				}
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
			throw new RestartException();
		}

		Tag form = htmlDocument.getSimpleTag("form").get(0);
		String action = form.getAttribute("action");
		this.setDirectUrl(new URL(action));
		URLConnection urlc = this.getDirectUrl().openConnection();
		
		return urlc;
	}
}