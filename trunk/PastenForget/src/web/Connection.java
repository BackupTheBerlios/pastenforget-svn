package web;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import parser.Tag;

/**
 * Diese Klasse dient als HTTP Verbindungsmanager. Es können POST und GET
 * Requests ausgeführt werden. Und der Response kann in verschiedenen Formaten
 * returnt werden. Durch das Dekorator Entwurfsmuster ist Cookie-Weiterleitung
 * möglich. (URLConnection)
 * 
 * @author christopher
 * 
 */
public class Connection {
	private URLConnection connection;
	private boolean cookieForwarding;
	private String cookie = null;
	
	
	public Connection() {
		this.cookieForwarding = true;
	}

	public Connection(boolean cookieForwarding) {
		this.cookieForwarding = cookieForwarding;
	}

	/**
	 * Setter für Cookie Weiterleitung
	 * 
	 * @param cookieForwarding
	 */
	public void setCookieForwarding(boolean cookieForwarding) {
		this.cookieForwarding = cookieForwarding;
	}

	/**
	 * Stellt eine Verbindung mit dem Server her, der durch die URL adressiert
	 * wird.
	 * 
	 * @param url
	 * @throws IOException
	 */
	public void connect(URL url) throws IOException {
		if (this.connection != null && this.cookieForwarding) {
			List<String> cookies = this.connection.getHeaderFields().get("Set-Cookie");
			if (cookies != null && cookies.size() > 0) {
				for(String cookie : cookies) {
					cookie = cookie.substring(0, cookie.indexOf(";"));
					this.cookie = cookie;
				}
				//String cookie = cookies.get(cookies.size() - 1);
				//this.cookie = this.readCookie(cookie);
				//System.out.println(this.cookie);
			}
		}
		
		this.connection = url.openConnection();
		if (this.cookieForwarding && this.cookie != null) {
			this.connection.addRequestProperty("Cookie", this.cookie);
			System.out.println(this.cookie);
		}
		this.connection.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.9) Gecko/2009042113 Ubuntu/8.10 (intrepid) Firefox/3.0.9");
	}
	
	/**
	 * Stellt eine Verbindung mit dem Server her, der durch den Link adressiert
	 * wird.
	 * 
	 * @param link
	 * @throws IOException
	 */
	public void connect(String link) throws IOException {
		this.connect(new URL(link));
	}

	public Map<String, List<String>> getHeaderFields() {
		return this.connection.getHeaderFields();
	}
	
	/**
	 * Getter für InputStream
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException {
		return this.connection.getInputStream();
	}

	/*
	 * private OutputStream getOutputStream() throws IOException { return
	 * this.connection.getOutputStream(); }
	 */

	/**
	 * Getter für den Response als Tag
	 */
	public Tag getDocument(boolean displayOutput) throws IOException {
		return this.readInputStream(displayOutput);
	}

	/**
	 * Getter für den Response als String
	 * 
	 * @return
	 * @throws IOException
	 */
	public Tag getDocument() throws IOException {
		return this.readInputStream(false);
	}

	/**
	 * Wandelt die Map postParameters in einen Query-String um.
	 * 
	 * @param postParameters
	 * @return
	 */
	private String createQuery(Map<String, String> postParameters) {
		StringBuffer query = new StringBuffer();
		Set<Map.Entry<String, String>> entrySet = postParameters.entrySet();
		for (Map.Entry<String, String> entry : entrySet) {
			query.append("&" + entry.getKey() + "=" + entry.getValue());
		}
		return (query.length() > 0) ? query.toString().substring(1)
				: new String();
	}

	/**
	 * Führt einen POST Request aus mit den Parametern der Map postParameters.
	 * Die Vorraussetzung für einen erfolgreichen POST Request ist die vorherige
	 * Ausführung der Methode connect(String link) oder connect(URL url)
	 * 
	 * @param postParameters
	 * @throws IOException
	 */
	public void doPost(Map<String, String> postParameters) throws IOException {
		String query = this.createQuery(postParameters);
		String contentLength = String.valueOf(query.length());
		String contentType = "application/x-www-form-urlencoded";

		this.connection.setUseCaches(true);
		this.connection.setDefaultUseCaches(true);
		this.connection.setDoInput(true);
		this.connection.setDoOutput(true);
		this.connection.setRequestProperty("Content-Type", contentType);
		this.connection.setRequestProperty("Content-Length", contentLength);

		OutputStream oStream = connection.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(oStream);
		writer.write(query);
		writer.flush();
		writer.close();
	}

	/**
	 * Getter für den Response als Image
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public Image getImage(String path) throws IOException {
		return ImageIO.read(this.getInputStream());
	}

	
	/**
	 * Liest den Response-Stream und wandelt ihn in das Tag Format
	 * 
	 * @param displayOutput
	 * @return
	 * @throws IOException
	 */
	private Tag readInputStream(boolean displayOutput) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(this
				.getInputStream(), "ISO-8859-1"));
		StringBuffer page = new StringBuffer();
		String currentLine = new String();
		while ((currentLine = reader.readLine()) != null) {
			if (displayOutput) {
				System.out.println(currentLine);
			}
			page.append(currentLine);
		}
		return new Tag(page.toString());
	}

	public URL getURL() {
		return this.connection.getURL();
	}
}
