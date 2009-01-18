package searchWebsite;

import java.net.URL;
import java.util.List;
import java.util.Observable;

/**
 * Abstraktes Suchobjekt. Definiert Standard-Funktionen.
 * 
 * @author executor
 *
 */
public abstract class SearchWebsite extends Observable {

	/**
	 * Startet den Suchvorgang.
	 * 
	 * @param keyWord Suchbegriff
	 */
	public abstract void search(String keyWord);
	
	/**
	 * Stoppt den Suchvorgang.
	 * 
	 * @return true, wenn Suchvorgang erfolgereich gestoppt wurde.
	 */
	public abstract boolean stopSearch();
	
	/**
	 * Filter die Downloadlinks aus einer Details-Seite.
	 * 
	 * @param url URL der Details-Seite
	 * @return Liste von URLs
	 */
	public abstract List<URL> filter(URL url);
	
	/**
	 * Gibt den Namen der Such-Website zurück.
	 * 
	 * @return Name der Such-Website
	 */
	public abstract String toString();
	
	/**
	 * Zeigt Details eines Suchergebnisses an.
	 * 
	 * @param url URL der Details-Seite
	 */
	public abstract void showDetails(URL url);
}
