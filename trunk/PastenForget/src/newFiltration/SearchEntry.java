package newFiltration;

public class SearchEntry {
	private final String link;
	private final String name;
	private final String date;
	private final String website;

	public SearchEntry(String link, String name, String date, String website) {
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
	
	@Override
	public String toString() {
		return "[" + this.link + ", " + this.name + ", " + this.date + ", " + this.website + "]";
	}
	
}
