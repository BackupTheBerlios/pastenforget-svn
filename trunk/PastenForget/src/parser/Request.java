package parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class Request {
	private FormProperties properties;
	private Map<String, List<String>> responseHeader = null;
	private List<String> cookies = null;

	/**
	 * Klasse zur Durchführung eines POSTs/GETs.
	 * 
	 * @param properties
	 */
	public Request(FormProperties properties) {
		this.properties = properties;
	}

	/**
	 * Setzt den Cookie.
	 * 
	 * @return
	 */
	public void setCookie(List<String> cookies) {
		this.cookies = cookies;

	}

	/**
	 * Gibt den Response-Header zurück.
	 * 
	 * @return
	 */
	public Map<String, List<String>> getResponseHeader() {
		return this.responseHeader;
	}

	/**
	 * Konvertiert die Elemente der RequestParameter-Map in einen
	 * ParameterString.
	 * 
	 * @return encodedParameters
	 * @throws UnsupportedEncodingException
	 */
	private String encodeParameters() throws UnsupportedEncodingException {
		String encodedParameters = new String();
		Map<String, String> parameters = this.properties.getParameters();
		if (parameters.isEmpty()) {
			return new String();
		} else {
			for (String name : parameters.keySet()) {
				encodedParameters += "&" + name + "="
						+ URLEncoder.encode(parameters.get(name), "iso-8859-1");
			}
			System.out.println(encodedParameters);
			return encodedParameters.substring(1);
		}
	}

	/**
	 * Führt einen Post durch und fügt, falls gesetzt, einen Request-Header
	 * hinzu.
	 * 
	 * @return response
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream post() throws MalformedURLException, IOException {
		URL url = new URL(this.properties.getAction());
		URLConnection connection = url.openConnection();
		if (this.cookies != null) {
			for (String cookie : this.cookies) {
				connection.addRequestProperty("Set-Cookie", cookie);
			}
		}
		String encodedParameters = this.encodeParameters();
		String length = String.valueOf(encodedParameters.length());

		connection.setUseCaches(true);
		connection.setDefaultUseCaches(true);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", length);

		OutputStream os = connection.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(os);
		writer.write(encodedParameters);
		writer.flush();
		writer.close();

		this.responseHeader = connection.getHeaderFields();
		return connection.getInputStream();
	}

	/**
	 * Führt einen GET durch und fügt, falls gesetzt, einen Request-Header
	 * hinzu.
	 * 
	 * @return response
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream get() throws MalformedURLException, IOException {
		String encodedParameters = this.encodeParameters();
		String link = this.properties.getAction();

		if (encodedParameters.length() != 0) {
			link += "?" + encodedParameters;
		}
		URL url = new URL(link);
		URLConnection connection = url.openConnection();
		if (this.cookies != null) {
			for (String cookie : this.cookies) {
				connection.addRequestProperty("Set-Cookie", cookie);
			}
		}

		this.responseHeader = connection.getHeaderFields();
		return connection.getInputStream();
	}

}
