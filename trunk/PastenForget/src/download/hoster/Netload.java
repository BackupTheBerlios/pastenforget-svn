package download.hoster;

import java.io.File;
import java.net.URL;

import queue.Queue;
import download.Download;
import download.DownloadInterface;

public class Netload extends Download implements DownloadInterface {

	public Netload(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName("netload");
	}

	@Override
	public void run() {
		this.stop();
	}

}
