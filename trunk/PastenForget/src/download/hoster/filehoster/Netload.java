package download.hoster.filehoster;

import java.io.File;
import java.net.URL;

import queue.Queue;
import download.Download;
import download.DownloadInterface;
import download.Status;

public class Netload extends Download implements DownloadInterface {

	public Netload() {
		super();
	}

	@Override
	public void setInformation(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus(Status.getWaiting());
		this.setFileName("netload");
	}

	@Override
	public void run() {
		this.stop();
	}

}
