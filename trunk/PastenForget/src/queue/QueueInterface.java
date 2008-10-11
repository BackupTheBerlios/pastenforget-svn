package queue;

import java.util.List;

import download.Download;

/**
 * Diese Schnittstellen muessen von jeder Warteschlange bereitgestellt werden.
 * 
 * @author executor
 * 
 */
public interface QueueInterface {

	/**
	 * Gibt eine Liste mit alle Downloads zurueck.
	 * 
	 * @return List<Download>
	 */
	public List<Download> getDownloadList();
	
	/**
	 * Fuegt einen Download an die Warteschlange an.
	 */
	public void addDownload(Download download);
	
	/**
	 * Gibt den derzeiten Download zurueck.
	 * 
	 * @return Download
	 */
	public Download getCurrent();

	/**
	 * Entfernt den derzeitigen Download.
	 */
	public void removeCurrent();
	
	/**
	 * Beendet und entfernt den Download an der Stelle index.
	 * 
	 * @param index
	 */
	public void removeDownload(int index);
	
	/**
	 * Beendet und entfernt den Downloads.
	 * 
	 * @param index
	 */
	public void removeDownloads(int []index);
	
	/**
	 * Stoppt den Download an der Stelle index.
	 * 
	 * @param index
	 */
	public void stopDownload(int index);
	
	/**
	 * Stoppt den Downloads.
	 * 
	 * @param index
	 */
	public void stopDownloads(int []index);
	
	/**
	 * Startet den Download an der Stelle index.
	 * 
	 * @param index
	 */
	public void startDownload(int index);
	
	/**
	 * Wenn getCurrent nicht gestartet wurde, wird dieser gestartet.
	 */
	public void startFirst();
	
	/**
	 * Prueft ob die Warteschlange leer ist.
	 * 
	 * @return boolean
	 */
	public boolean isEmpty();
}
