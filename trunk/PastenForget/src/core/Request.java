package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Request {
	private String action = "";
	private Map<String,String> parameters = new HashMap<String,String>();
	
	public Request() {
	}
	
	public Request(String action) {
		this.action = action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public void setParameters(Map<String,String> parameters) {
		this.parameters = parameters;
	}
	
	public void addParameter(String name, String value) {
		this.parameters.put(name, value);
	}
	
	private String encodeParameters() throws UnsupportedEncodingException {
		Set<Map.Entry<String,String>> set = this.parameters.entrySet();
		Iterator<Map.Entry<String,String>> it = set.iterator();
		String encodedParameters = new String();
		
		while(it.hasNext()) {
			Map.Entry<String,String> current = it.next();
			encodedParameters += "&" + current.getKey() + "=" + URLEncoder.encode(current.getValue(), "iso-8859-1");
		}
		
		return encodedParameters.substring(1);
	}
	
	public InputStream request() throws MalformedURLException, IOException {
		URL url = new URL(this.action);
		URLConnection urlc = url.openConnection();
		String encodedParameters = encodeParameters();
		String length = String.valueOf(encodedParameters.length());

		urlc.setUseCaches(true);
		urlc.setDefaultUseCaches(true);
		urlc.setDoInput(true);
		urlc.setDoOutput(true);
		urlc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		urlc.setRequestProperty("Content-Length", length);
		
		OutputStream os = urlc.getOutputStream();
		OutputStreamWriter requestWriter = new OutputStreamWriter(os);
		requestWriter.write(encodedParameters);
		requestWriter.flush();
		requestWriter.close();

		URLConnection postMethodResponse = urlc;
		
		return postMethodResponse.getInputStream();
	}

}
