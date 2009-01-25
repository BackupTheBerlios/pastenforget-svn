package download;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.Observable;

import middleware.ObserverMessageObject;

/**
 * Abstraktes Downloadobjekt. Stellt Basisvariablen und Funktionen bereit.
 * 
 * @author executor
 *
 */

public abstract class Download  extends Observable implements Runnable {

	private boolean start = false;
	private boolean stop = false;
	private boolean wait = true;
	private URL url = null;
	private String fileName = new String();
	private File destination = null;
	private long currentSize;
	private long expectedSize;
	private DownloadThread thread = null;
	private String status = new String();
	private Image captcha = null;
	private String captchaCode = "";
	private int priority = 0;

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		if (start) {
			this.setStop(false);
			this.setWait(false);
		}
		this.start = start;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		if (stop) {
			this.setStart(false);
			this.setWait(false);
		}
		this.stop = stop;
	}

	public boolean isWait() {
		return wait;
	}

	public void setWait(boolean wait) {
		if (wait) {
			this.setStop(false);
			this.setStart(false);
		}
		this.wait = wait;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	protected void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getDestination() {
		return destination;
	}

	protected void setDestination(File destination) {
		this.destination = destination;
	}

	public long getCurrentSize() {
		return currentSize;
	}

	protected void setCurrentSize(long currentSize) {
		this.currentSize = currentSize;
	}

	public long getExpectedSize() {
		return expectedSize;
	}

	protected void setExpectedSize(long expectedSize) {
		this.expectedSize = expectedSize;
	}

	protected DownloadThread getThread() {
		return thread;
	}

	protected void setThread(DownloadThread thread) {
		this.thread = thread;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		setChanged();
		ObserverMessageObject omo = new ObserverMessageObject(this);
		notifyObservers(omo);
	}

	public Image getCaptcha() {
		return captcha;
	}

	public void setCaptcha(Image captcha) {
		this.captcha = captcha;
		setChanged();
		ObserverMessageObject omo = new ObserverMessageObject(this, true);
		notifyObservers(omo);
	}
	
	public String getCaptchaCode() {
		return captchaCode;
	}

	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
	/**
	 * Stoppt Download und setzt Flags für "Warten".
	 * 
	 * @return true, wenn Download erfolgreich gestoppt wurde
	 */
	public boolean waiting() {
		boolean result = this.stop();
		if (result) {
			this.setStop(false);
			this.setStart(false);
			this.setWait(true);
		}
		return result;
	}
	
	/**
	 * Startet den Downloadvorgang und setzt Flags für "Gestartet".
	 * 
	 * @return true, wenn Download erfolgreich gestartet wurde
	 */
	public abstract boolean start();

	/**
	 * Stoppt den Downloadvorgang und setzt Flags für "Gestoppt"
	 * 
	 * @return true, wenn Download erfolgreich gestoppt wurde
	 */
	public abstract boolean stop();

	/**
	 * Bricht den Download ab.
	 * 
	 * @return true, wenn Download erfolgreich abgebrochen wurde
	 */
	public abstract boolean cancel();

}
