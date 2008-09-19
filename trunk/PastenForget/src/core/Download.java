package core;

import java.net.URL;
import java.util.Observable;

/**
 * Allgemeines Downloadobjekt. Vererbt an spezielle Hoster.
 * 
 * @author cpieloth
 * 
 */
public class Download extends Observable {

	private String filename = "unbekannt";

	private int fileSize = 0;

	private String status = "Warten";

	private int currentSize = 0;

	private URL url;

	private Queue queue;

	protected ServerDownload serverDownload = null;

	protected StopThread stopThread = new StopThread();

	public String getFilename() {
		return filename;
	}

	public void setFilename(String name) {
		this.filename = name;
		setChanged();
		notifyObservers("downloadFileName");
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
		setChanged();
		notifyObservers("downloadFileSize");
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		setChanged();
		notifyObservers("downloadStatus");
	}

	public int getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
		setChanged();
		notifyObservers("downloadCurrentSize");
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public boolean stop() {
		this.stopThread.stopThread();
		return false;
	}

	public boolean start() {
		this.setStatus("Warten");
		System.out.println("start: core.hoster/...");
		new DownloadThread((DownloadInterface) this).start();
		return true;
	}
}
