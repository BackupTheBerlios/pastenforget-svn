package searchWebsite;

import java.awt.Desktop;
import java.io.IOException;
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
	 * @throws IOException 
	 */
	public abstract void search(String keyWord) throws IOException;
	
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
	 * @throws IOException 
	 */
	public abstract List<URL> filter(URL url) throws IOException;
	
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
	public void showDetails(URL url) {
		try {
			Desktop.getDesktop().browse(url.toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
