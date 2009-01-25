package newFiltration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;

public abstract class SearchWebsite extends Observable {

	public abstract void search(String keyWord) throws IOException;
	
	public abstract List<URL> filter(URL url) throws IOException;
	
	public abstract void showDetails(URL url);
}
