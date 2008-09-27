package core;

import java.io.IOException;
import java.net.URL;

/**
 * Diese Schnittstellen muessen von jedem Hoster bereitgestellt werden.
 * 
 * @author cpieloth
 * 
 */
public interface DownloadInterface {

	/**
	 * Gibt den Dateinamen zurueck.
	 * 
	 * @return
	 */
	public String getFilename();

	/**
	 * Setzt den Dateinamen.
	 * 
	 * @param name
	 */
	public void setFilename(String name);

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

	public void downloadFileFromHoster() throws IOException;

}