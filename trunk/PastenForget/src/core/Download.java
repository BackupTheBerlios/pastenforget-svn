package core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import core.hoster.NodeListIterator;

/**
 * Allgemeines Downloadobjekt. Vererbt an spezielle Hoster.
 * 
 * @author cpieloth
 * 
 */
public class Download extends Observable {

	private String filename = "unbekannt";

	private long fileSize = 0;

	private String status = "Warten";

	private long currentSize = 0;

	private URL url, directUrl;

	private Queue queue;

	protected ServerDownload serverDownload = null;

	protected StopThread stopThread = new StopThread();

	public String getFilename() {
		return filename;
	}

	public void setFilename(String name) {
		this.filename = name;
		setChanged();
		notifyObservers("downloadFileName");
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
		setChanged();
		notifyObservers("downloadFileSize");
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		setChanged();
		notifyObservers("downloadStatus");
	}

	public long getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
		setChanged();
		notifyObservers("downloadCurrentSize");
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
	public URL getDirectUrl() {
		return directUrl;
	}

	public void setDirectUrl(URL directUrl) {
		this.directUrl = directUrl;
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public boolean stop() {
		this.stopThread.stopThread();
		return false;
	}

	public boolean start() {
		this.setStatus("Warten");
		System.out.println("start: core.hoster/...");
		new DownloadThread((DownloadInterface) this).start();
		return true;
	}
	
	public boolean startDownload() throws MalformedURLException, IOException {
		return false;
	}
	
	/*
	 * Diese Methode ermittelt aus einer gegebenen Form alle f�r einen RequestParameter
	 *  erforderlichen Attribute, exkl. Captchacode
	 *  	@param form, requestParameters
	 */
	protected void getRequestParameters(Node form, Map<String, String> requestParameters) {
		if(form.hasChildNodes()) {
			Iterator<Node> InputIterator = new NodeListIterator(form.getChildNodes());
			while(InputIterator.hasNext()) {
				Node current = InputIterator.next();
	    		Node nameNode = current.getAttributes().getNamedItem("name");
				Node valueNode = current.getAttributes().getNamedItem("value");
				if(nameNode != null) {
					String parameterValue = (valueNode != null) ? valueNode.getNodeValue() : "";
					String parameterName = nameNode.getNodeValue();
					requestParameters.put(parameterName, parameterValue);
				}
				getRequestParameters(current, requestParameters);
	    	}	
	    }
	}
	
	/*
	 * Diese Methode codiert alle in der requestParameters-Map enthaltenen Attribute in eine f�r
	 *  den PostRequest erforderliche Form.
	 *  	@param requestParameters
	 *  	@return encodedParameters
	 */
	protected String encodeParamters(Map<String,String> requestParameters) throws UnsupportedEncodingException {
		Set<Map.Entry<String,String>> set = requestParameters.entrySet();
		Iterator<Map.Entry<String,String>> it = set.iterator();
		String encodedParameters = new String();
		
		while(it.hasNext()) {
			Map.Entry<String,String> current = it.next();
			encodedParameters += "&" + current.getKey() + "=" + URLEncoder.encode(current.getValue(), "iso-8859-1");
		}
		
		return encodedParameters.substring(1);
	}
	
	/*
	 * Diese Methode fuehrt einen Post - Request aus mit �bergabe von Parametern
	 * 		@param action, parameterString
	 * 		@return postMethodResponse
	 */
	protected URLConnection request(String action, String parameterString) throws MalformedURLException, IOException {
		URL url = new URL(action);
		URLConnection urlc = url.openConnection();
		String length = String.valueOf(parameterString.length());

		urlc.setUseCaches(true);
		urlc.setDefaultUseCaches(true);
		urlc.setDoInput(true);
		urlc.setDoOutput(true);
		urlc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		urlc.setRequestProperty("Content-Length", length);
		
		OutputStream os = urlc.getOutputStream();
		OutputStreamWriter requestWriter = new OutputStreamWriter(os);
		requestWriter.write(parameterString);
		requestWriter.flush();
		requestWriter.close();

		URLConnection postMethodResponse = urlc;
		
		return postMethodResponse;
	}
	
	/*
	 * Diese Methode ermittelt die Quelle der Bilddatei des Captchas
	 * 		@param domTree, imageID
	 * 		@return source
	 */
	protected String getCaptchaImage(Document domTree, String imageID) {
		NodeList imageNodes = domTree.getElementsByTagName("img");
		Pattern p = Pattern.compile(imageID);
		
		for(int i = 0; i < imageNodes.getLength() ; i++) {
			Node src = imageNodes.item(i).getAttributes().getNamedItem("src");
			if(src != null) {
				if(p.matcher(src.getNodeValue()).find()) {
					String source = src.getNodeValue();
					return source;
				}
			}
		}
		return null;
	}
}
