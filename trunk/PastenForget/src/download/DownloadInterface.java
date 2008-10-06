package download;

import java.io.File;
import java.net.URL;

import queue.Queue;
import exception.CancelException;
import exception.StopException;

/**
 * Diese Schnittstellen muessen von jedem Hoster bereitgestellt werden.
 * 
 * @author cpieloth
 * 
 */
public interface DownloadInterface {

	/**
	 * Gibt den Downloadpfad zurueck.
	 * 
	 * @return
	 */
	public File getDestination();

	/**
	 * Setzt den Downloadpfad.
	 * 
	 * @param name
	 */
	public void setDestination(File destination);

	/**
	 * Gibt den Dateinamen zurueck.
	 * 
	 * @return
	 */
	public String getFileName();

	/**
	 * Setzt den Dateinamen.
	 * 
	 * @param name
	 */
	public void setFileName(String fileName);

	/**
	 * Gibt die Dateigroesze zurueck.
	 * 
	 * @return
	 */
	public long getFileSize();

	/**
	 * Setzt die Dateigroesze.
	 * 
	 * @param fileSize
	 */
	public void setFileSize(long fileSize);

	/**
	 * Gibt den Status des Downloads zurueck.
	 * 
	 * @return String Warten, Zeit bis Beginn oder Aktiv
	 */
	public String getStatus();

	/**
	 * Setzt den Status eines Downloads.
	 * 
	 * @param status
	 *            Warten, Zeit bis Beginn oder Aktiv
	 */
	public void setStatus(String status);

	/**
	 * Gibt die aktuelle Dateigroesze zurueck um den Fortschritt anzuzeigen.
	 * 
	 * @return
	 */
	public long getCurrentSize();

	/**
	 * Setzt die aktuelle Dateigroesze, KB die bis jetzt geladen wurden.
	 * 
	 * @param currentSize
	 */
	public void setCurrentSize(long currentSize);

	/**
	 * Gibt die URL des Downloadlinks zurueck.
	 * 
	 * @return
	 */
	public URL getUrl();

	/**
	 * Setzt die URL des Downloadlinks.
	 * 
	 * @param url
	 */
	public void setUrl(URL url);

	/**
	 * Gibt die URL des direkten Downloadlinks zurueck.
	 * 
	 * @return
	 */
	public URL getDirectUrl();

	/**
	 * Setzt die URL des direkten Downloadlinks.
	 * 
	 * @param url
	 */
	public void setDirectUrl(URL directUrl);

	/**
	 * Gibt die Warteschlange des Downloads bzw. Hosters zurueck.
	 * 
	 * @return
	 */
	public Queue getQueue();

	/**
	 * Setzt die Warteschlange des Downloads bzw. Hosters.
	 * 
	 * @param queue
	 */
	public void setQueue(Queue queue);

	/**
	 * Gibt den Index in der Warteschlange zurueck.
	 * 
	 * @return
	 */
	public int getIndex();

	/**
	 * Gibt true zurueck, wenn Download gestartet wurde.
	 * 
	 * @return
	 */
	public boolean isStarted();

	/**
	 * Setzt started.
	 * 
	 * @return
	 */
	public void setStarted(boolean started);

	/**
	 * Gibt true zurueck, wenn Download gestoppt ist.
	 * 
	 * @return
	 */
	public boolean isStopped() throws StopException;

	/**
	 * Setzt canceled.
	 * 
	 * @return
	 */
	public void setCanceled(boolean canceled);
	
	/**
	 * Gibt true zurueck, wenn Download abgebrochen wurde.
	 * 
	 * @return
	 */
	public boolean isCanceled() throws CancelException;

	
	/**
	 * Setzt stopped.
	 * 
	 * @return
	 */
	public void setStopped(boolean stopped);

	/**
	 * Startet den Download.
	 * 
	 * @return
	 */
	public boolean start();

	/**
	 * Stoppt den Download.
	 * 
	 * @return
	 */
	public boolean stop();

	/**
	 * Stoppt den Download und loescht die File.
	 * 
	 * @return
	 */
	public boolean cancel();

}