package web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import parser.Tag;

public class Connection {
	private URLConnection connection;
	private boolean cookieForwarding = false;
	private String cookie;

	public void connect(String link) throws IOException {
		URL url = new URL(link);
		if (connection != null && this.cookieForwarding) {
			String cookieField = this.connection.getHeaderField("Set-Cookie");
			if(cookieField != null) {
				this.cookie = readCookie(cookieField);
			}
		}
		this.connection = url.openConnection();
	}
	
	public void connect(URL url) throws IOException {
		if (connection != null && this.cookieForwarding) {
			String cookie = readCookie(this.connection.getHeaderField("Set-Cookie"));
			System.out.println(cookie);
		}
		this.connection = url.openConnection();
	}
	
	private String readCookie(String cookieField) {
		int seperatorIndex = cookieField.indexOf(";");
		String cookie = (seperatorIndex > -1) ? cookieField.substring(0, seperatorIndex) : new String();
		return cookie;
	}

	public InputStream getInputStream() throws IOException {
		return this.connection.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return this.connection.getOutputStream();
	}

	private String createQuery(Map<String, String> postParameters) {
		StringBuffer query = new StringBuffer();
		Set<Map.Entry<String, String>> entrySet = postParameters.entrySet();
		for (Map.Entry<String, String> entry : entrySet) {
			query.append("&" + entry.getKey() + "=" + entry.getValue());
		}
		return (query.length() > 0) ? query.toString().substring(1)
				: new String();
	}

	public Tag doGet() throws IOException {
		InputStream iStream = this.connection.getInputStream();
		Tag document = this.readInputStream(iStream);
		return document;
	}

	public Tag doPost(Map<String, String> postParameters) throws IOException {
		String query = this.createQuery(postParameters);
		String contentLength = String.valueOf(query.length());
		String contentType = "application/x-www-form-urlencoded";
		String cookie = (this.cookieForwarding) ? this.cookie : new String();
		this.connection.setUseCaches(true);
		this.connection.setDefaultUseCaches(true);
		this.connection.setDoInput(true);
		this.connection.setDoOutput(true);
		this.connection.setRequestProperty("Content-Type", contentType);
		this.connection.setRequestProperty("Content-Length", contentLength);
		this.connection.setRequestProperty("Cookie", cookie);
		OutputStream oStream = connection.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(oStream);
		writer.write(query);
		writer.flush();
		writer.close();
		InputStream iStream = this.connection.getInputStream();
		Tag document = this.readInputStream(iStream);
		return document;
	}

	private Tag readInputStream(InputStream iStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				iStream));
		StringBuffer page = new StringBuffer();
		String currentLine = new String();
		while ((currentLine = reader.readLine()) != null) {
			page.append(currentLine);
		}

		return new Tag(page.toString());
	}
	
}
