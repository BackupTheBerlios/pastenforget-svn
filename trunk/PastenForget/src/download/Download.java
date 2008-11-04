package download;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.Observable;

import middleware.ObserverMessageObject;
import queue.Queue;
import exception.CancelException;
import exception.StopException;
import filtration.RequestPackage;

/**
 * Allgemeines Downloadobjekt. Vererbt an spezielle Hoster.
 * 
 * @author executor
 * 
 */
public abstract class Download extends Observable implements DownloadInterface, Runnable {

	private RequestPackage requestPackage = null;
	
	private File destination = null;

	private String fileName = "unbekannt";

	private long fileSize = 0;

	private String status = Status.getWaiting();
	
	private double averageSpeed = 0;

	private long currentSize = 0;

	private URL url, directUrl;

	private Queue queue;

	private Image captcha = null;
	
	private String captchaCode = "";

	private boolean isStarted = false;

	private boolean isStopped = false;

	private boolean isCanceled = false;

	protected Thread thread = null;
	
	public abstract void setInformation(URL url, File destination, Queue queue); 
	
	@Override
	public void setIrc(RequestPackage requestPackage) {
		this.requestPackage = requestPackage;
	}

	@Override
	public RequestPackage getIrc() {
		return requestPackage;
	}
	
	@Override
	public void setDestination(File destination) {
		this.destination = destination;
	}

	@Override
	public File getDestination() {
		return destination;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
		setChanged();
		ObserverMessageObject omo = new ObserverMessageObject(this);
		notifyObservers(omo);
	}

	@Override
	public long getFileSize() {
		return fileSize;
	}

	@Override
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
		setChanged();
		ObserverMessageObject omo = new ObserverMessageObject(this);
		notifyObservers(omo);
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
		setChanged();
		ObserverMessageObject omo = new ObserverMessageObject(this);
		notifyObservers(omo);
	}

	public double getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	@Override
	public long getCurrentSize() {
		return currentSize;
	}

	@Override
	public void setCurrentSize(long currentSize) {
		this.currentSize = currentSize;
		setChanged();
		ObserverMessageObject omo = new ObserverMessageObject(this);
		notifyObservers(omo);
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public void setUrl(URL url) {
		this.url = url;
	}

	@Override
	public URL getDirectUrl() {
		return directUrl;
	}

	@Override
	public void setDirectUrl(URL directUrl) {
		this.directUrl = directUrl;
	}

	@Override
	public Queue getQueue() {
		return queue;
	}

	@Override
	public void setQueue(Queue queue) {
		this.queue = queue;
		ObserverMessageObject omo = new ObserverMessageObject(this);
		notifyObservers(omo);
	}

	@Override
	public Image getCaptcha() {
		return captcha;
	}

	@Override
	public void setCaptcha(Image captcha) {
		this.captcha = captcha;
		setChanged();
		ObserverMessageObject omo = new ObserverMessageObject(this, true);
		notifyObservers(omo);
	}

	@Override
	public String getCaptchaCode() {
		return captchaCode;
	}

	@Override
	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}

	@Override
	public synchronized boolean start() {
		this.setStatus(Status.getStarted());
		this.setStarted(true);
		this.setStopped(false);
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		return true;
	}

	@Override
	public synchronized boolean stop() {
		if (thread != null)
			thread = null;
		this.setCurrentSize(0);
		this.setStatus(Status.getStopped());
		this.setStopped(true);
		this.setStarted(false);
		return true;
	}

	@Override
	public synchronized boolean cancel() {
		if (thread != null)
			thread = null;
		this.setStatus(Status.getCanceled());
		this.setCanceled(true);
		this.setStarted(false);
		if (this.isStopped) {
			String filename = new String();
			if (this.getDestination() == null) {
				filename = this.getFileName();
			} else {
				filename = this.getDestination().getPath() + File.separator
						+ this.getFileName();
			}
			File file = new File(filename);
			if (file.exists()) {
				file.delete();
			}
			System.out.println("Download canceled: " + this.getFileName());
		}
		return true;
	}

	public void wait(int waitingTime) throws StopException, CancelException {
		try {
			while (waitingTime > 0) {
				if (this.isStopped) {
					throw new StopException();
				}
				if (this.isCanceled) {
					throw new CancelException();
				}
				this.setStatus(Status.getWaitSec(waitingTime--));
				Thread.sleep(1000);
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * Führt alle nötigen Schritte durch, die für den Download einer Datei
	 * notwendig sind.
	 */
	@Override
	public void run() {
	}

	@Override
	public int getIndex() {
		return queue.getDownloadList().indexOf(this);
	}

	@Override
	public boolean isStarted() {
		return isStarted;
	}

	@Override
	public void setStarted(boolean started) {
		isStarted = started;
	}

	@Override
	public synchronized boolean isStopped() {
		return this.isStopped;
	}

	@Override
	public synchronized void setStopped(boolean stopped) {
		this.isStopped = stopped;
	}

	@Override
	public synchronized boolean isCanceled() {
		return this.isCanceled;
	}

	@Override
	public synchronized void setCanceled(boolean canceled) {
		this.isCanceled = canceled;
	}
	
}
