package searchWebsite;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * Objekt für ein Suchergebnis.
 * 
 * @author executor
 *
 */
public class SearchEntry {
	private final SearchWebsite searchWebsite;
	private final String link;
	private final String name;
	private final String date;
	private final String website;

	/**
	 * @param searchWebsite Referenz zum Suchobjekt
	 * @param link URL zur Details-Seite des Suchergebnisses
	 * @param name Titel/ Name des des Suchergebnisses
	 * @param date Datum des Sucheintrags
	 * @param website Name der Such-Website
	 */
	public SearchEntry(SearchWebsite searchWebsite, String link, String name, String date, String website) {
		this.searchWebsite = searchWebsite;
		this.link = link;
		this.name = name;
		this.date = date;
		this.website = website;
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return date;
	}

	public String getWebsite() {
		return website;
	}
	
	public List<URL> getLinks() throws IOException {
		URL url;
		try {
			url = new URL(this.link);
			return this.searchWebsite.filter(url);
		} catch (MalformedURLException e) {
			System.out.println("SearchEntry.getLinks: false URL");
			return null;
		}
	}
	
	public boolean showDetails() {
		try {
			Desktop.getDesktop().browse(
					new URI(this.getLink()));
			return true;
		} catch (Exception e) {
			System.out.println("SearchEntry: failure");
			return false;
		}
	}

}
