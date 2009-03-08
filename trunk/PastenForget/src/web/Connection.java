package web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
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

import parser.Tag;

public class Connection {
	private URLConnection connection;
	private boolean cookieForwarding = true;
	private String cookie = new String();

	public void setCookieForwarding(boolean cookieForwarding) {
		this.cookieForwarding = cookieForwarding;
	}

	public void connect(String link) throws IOException {
		URL url = new URL(link);
		if (connection != null && this.cookieForwarding) {
			List<String> cookies = this.connection.getHeaderFields().get(
					"Set-Cookie");
			if (cookies != null && cookies.size() > 0) {
				String cookie = cookies.get(cookies.size() - 1);
				this.cookie = this.readCookie(cookie);
			}
		}
		this.connection = url.openConnection();
	}

	public void connect(URL url) throws IOException {
		if (connection != null && this.cookieForwarding) {
			String cookie = readCookie(this.connection
					.getHeaderField("Set-Cookie"));
			System.out.println(cookie);
		}
		this.connection = url.openConnection();
	}

	private String readCookie(String cookieField) {
		int seperatorIndex = cookieField.indexOf(";");
		String cookie = (seperatorIndex > -1) ? cookieField.substring(0,
				seperatorIndex) : new String();
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

		if (this.cookieForwarding) {
			this.connection.addRequestProperty("Cookie", this.cookie);
		}
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
		InputStream iStream = this.connection.getInputStream();
		Tag document = this.readInputStream(iStream);
		return document;
	}

	public void getImage() throws IOException {
		String query = new String();
		String contentLength = String.valueOf(query.length());
		String contentType = "application/x-www-form-urlencoded";

		if (this.cookieForwarding) {
			this.connection.addRequestProperty("Cookie", this.cookie);
		}
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
		InputStream iStream = this.connection.getInputStream();
		oStream = new FileOutputStream(
				"/home/christopher/Desktop/netload.in/captcha.gif");
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = iStream.read(buffer)) > 0) {
			oStream.write(buffer, 0, len);
		}
		oStream.flush();
		oStream.close();
	}

	public Tag readInputStream(InputStream iStream) throws IOException {
		String filename = "/home/christopher/Desktop/netload.in/sourceCode_"
				+ System.currentTimeMillis() + ".html";
		OutputStream oStream = new FileOutputStream(filename);
		System.out.println(filename);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				oStream));
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				iStream));
		StringBuffer page = new StringBuffer();
		String currentLine = new String();
		while ((currentLine = reader.readLine()) != null) {
			writer.write(currentLine + "\n");
			page.append(currentLine);
		}
		writer.flush();
		writer.close();
		return new Tag(page.toString());
	}

}
