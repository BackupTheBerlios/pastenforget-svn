package searchWebsite;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OxygenWarez extends SearchWebsite {;

	@Override
	public List<URL> filter(URL url) throws IOException {
		// TODO filter()
		return new ArrayList<URL>();
	}

	@Override
	public void search(String keyWord) throws IOException {
		// TODO search
		SearchEntry entry = new SearchEntry(this, "http://test.test", "TEST", "2009",
		this.toString());
		this.setChanged();
		this.notifyObservers(entry);
	}

	@Override
	public boolean stopSearch() {
		// TODO stopSearch()
		return false;
	}

	@Override
	public String toString() {
		return "Oxygen Warez";
	}

}
