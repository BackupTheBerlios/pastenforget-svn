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

	public final URL url;
	private Map<String,List<String>> header;
	protected URLConnection urlConnection;

	public ServerConnection(URL url) {
		this.url = url;
	}

	public InputStream openDownloadStream() throws MalformedURLException, IOException {
		this.urlConnection = this.url.openConnection();
		this.header = this.urlConnection.getHeaderFields();
	/*
		for (Map.Entry<String,List<String>> pairs : this.header.entrySet()) {
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
		}
	*/
		InputStream is = this.urlConnection.getInputStream();
		return is;
	}

	public Map<String,List<String>> getHeader() throws MalformedURLException, IOException {
		return this.header;

	}

	protected OutputStream openUploadStream(String targetfile) throws MalformedURLException, IOException {;
		this.urlConnection = url.openConnection();
		OutputStream os = this.urlConnection.getOutputStream();
		return os;
	}

	protected void close() {
		this.urlConnection = null;
	}
}