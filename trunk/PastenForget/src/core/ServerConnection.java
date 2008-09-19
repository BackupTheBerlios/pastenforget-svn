package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Stellt eine Verbindung zum Webserver her.
 * 
 * @author cschaedl
 */

public class ServerConnection {

	public final String host;
	private Map<String,List<String>> header;
	protected URLConnection urlConnection;

	
	public ServerConnection(String Host) {
		host = Host;
	}

	protected URL createURL() throws MalformedURLException {
		return new URL(host);
	}

	public InputStream openDownloadStream() throws MalformedURLException, IOException {
		URL url = createURL();
		urlConnection = url.openConnection();
		this.header = urlConnection.getHeaderFields();
	
		for (Map.Entry<String,List<String>> pairs : this.header.entrySet()) {
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
		}
	
		InputStream is = urlConnection.getInputStream();
		return is;
	}

	public Map<String,List<String>> getHeader() throws MalformedURLException, IOException {
		return this.header;
		/*
		for (Map.Entry<String,List<String>> pairs : this.header.entrySet()) {
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
		}
		*/
	}

	protected OutputStream openUploadStream(String targetfile) throws MalformedURLException, IOException {
		URL url = createURL();
		urlConnection = url.openConnection();
		OutputStream os = urlConnection.getOutputStream();
		return os;
	}

	protected void close() {
		urlConnection = null;
	}
}