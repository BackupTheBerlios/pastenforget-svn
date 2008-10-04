package download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

import queue.Queue;
import stream.ServerDownload;

/**
 * Allgemeines Downloadobjekt. Vererbt an spezielle Hoster.
 * 
 * @author cpieloth
 * 
 */
public class Download extends Observable implements DownloadInterface, Runnable {

	private File destination = null;

	private String fileName = "unbekannt";

	private long fileSize = 0;

	private String status = "Warten";

	private long currentSize = 0;

	private URL url, directUrl;

	private Queue queue;
	
	private int index = -1;
	
	private boolean hasStarted = false;

	protected ServerDownload serverDownload = null;

	protected StopThread stopThread = new StopThread();

	public void setDestination(File destination) {
		this.destination = destination;
	}

	public File getDestination() {
		return destination;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		setChanged();
		notifyObservers("download");
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
		setChanged();
		notifyObservers("download");
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		setChanged();
		notifyObservers("download");
	}

	public long getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(long currentSize) {
		this.currentSize = currentSize;
		setChanged();
		notifyObservers("download");
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
		this.setStarted();
		new Thread((Runnable) this).start();
		return true;
	}

	public boolean startDownload() throws MalformedURLException, IOException {
		return false;
	}

	public void run() {
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public boolean hasStarted() {
		return hasStarted;
	}

	@Override
	public void setStarted() {
		hasStarted = true;
	}
}
