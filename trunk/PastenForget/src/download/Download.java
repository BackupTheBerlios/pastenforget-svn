package download;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

import core.Queue;
import core.ServerDownload;
import core.StopThread;


/**
 * Allgemeines Downloadobjekt. Vererbt an spezielle Hoster.
 * 
 * @author cpieloth
 * 
 */
public class Download extends Observable implements DownloadInterface, Runnable {

	private String fileName = "unbekannt";

	private long fileSize = 0;

	private String status = "Warten";

	private long currentSize = 0;

	private URL url, directUrl;

	private Queue queue;

	protected ServerDownload serverDownload = null;

	protected StopThread stopThread = new StopThread();

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		setChanged();
		notifyObservers("downloadFileName");
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
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

	public long getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(long currentSize) {
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
	
	public URL getDirectUrl() {
		return directUrl;
	}

	public void setDirectUrl(URL directUrl) {
		this.directUrl = directUrl;
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
		new Thread((Runnable)this).start();
		return true;
	}
	
	public boolean startDownload() throws MalformedURLException, IOException {
		return false;
	}
	
	public void run() {	
	}
}
