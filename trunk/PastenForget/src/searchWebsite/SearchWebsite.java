package searchWebsite;

import java.net.URL;
import java.util.List;
import java.util.Observable;

public abstract class SearchWebsite extends Observable {

	public abstract void search(String keyWord);
	
	public abstract List<URL> filter(URL url);
	
	public abstract void showDetails(URL url);
}
