package download;

import java.io.File;
import java.net.URL;

/**
 * Abstraktes Downloadobjekt. Stellt Basisvariablen und Funktionen bereit.
 * 
 * @author executor
 *
 */

public abstract class DownloadTest implements Runnable {

	private boolean start = false;
	private boolean stop = false;
	private boolean wait = true;
	private URL url = null;
	private String fileName = new String();
	private File destination = null;
	private long currentSize;
	private long expectedSize;
	private Thread thread = null;
	private int priority = 0;

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public boolean isWait() {
		return wait;
	}

	public void setWait(boolean wait) {
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

	protected Thread getThread() {
		return thread;
	}

	protected void setThread(Thread thread) {
		this.thread = thread;
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
	
	protected void download()  {
		// TODO
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
