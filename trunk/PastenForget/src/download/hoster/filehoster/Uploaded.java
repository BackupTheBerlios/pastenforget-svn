package download.hoster.filehoster;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import middleware.Tools;
import parser.Tag;
import queue.Queue;
import stream.ServerDownload;
import download.Download;
import download.DownloadInterface;
import exception.CancelException;
import exception.StopException;
import exception.TagNotSupportedException;

public class Uploaded extends Download implements DownloadInterface {
	private int counter = 0;

	public Uploaded(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.createFileName());
	}

	public String createFileName() {
		try {
			URL url = this.getUrl();
			InputStream in = url.openConnection().getInputStream();
			Tag titleTag = Tools.getTitleFromInputStream(in);
			String title = titleTag.getTagContent(false);
			return title;
		} catch (IOException ioError) {
			return this.getUrl().toString();
		} catch (TagNotSupportedException noValidTag) {
			return this.getUrl().toString();
		}
	}

	@Override
	public void run() {
		try {
			URL url = this.getUrl();
			InputStream in = url.openConnection().getInputStream();
			Tag htmlDocument = Tools.getTagFromInputStream(in, false);
			if (htmlDocument.toString().indexOf(
					"Your Free-Traffic is exceeded!") != -1) {
				this.setStatus("No Free Traffic - Versuch: " + ++counter);
				Thread.sleep(600000);
				this.run();
			}

			this.setDirectUrl(new URL(this.createFileName()));
			
			Tag form = htmlDocument.getSimpleTag("form").get(0);
			String action = form.getAttribute("action");
			this.setDirectUrl(new URL(action));

			if (this.isStopped()) {
				throw new StopException();
			}
			if (this.isCanceled()) {
				throw new CancelException();
			}
			ServerDownload.download(this);

		} catch(InterruptedException interrupted) {
			interrupted.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("Uploaded.to Seite nicht erreichbar");
		} catch (StopException stopped) {
			System.out.println("Download stopped: " + this.getFileName());
		} catch (CancelException canceled) {
			System.out.println("Download canceled: " + this.getFileName());
		}
	}
}