package searchWebsite;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class OxygenWarez extends SearchWebsite {;

	@Override
	public List<URL> filter(URL url) throws IOException {
		// TODO filter()
		return null;
	}

	@Override
	public void search(String keyWord) throws IOException {
		// TODO search()
	}

	@Override
	public void showDetails(URL url) {
		// TODO showDetails()

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
