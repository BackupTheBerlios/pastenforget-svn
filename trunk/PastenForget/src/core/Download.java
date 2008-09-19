package core;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.Observable;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import core.hoster.AwtImage;
import core.hoster.NodeListIterator;

/**
 * Allgemeines Downloadobjekt. Vererbt an spezielle Hoster.
 * 
 * @author cpieloth
 * 
 */
public class Download extends Observable {

	private String filename = "unbekannt";

	private int fileSize = 0;

	private String status = "Warten";

	private int currentSize = 0;

	private URL url;

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

	public int getFileSize() {
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

	public int getCurrentSize() {
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
	 * Diese Methode ermittelt aus einer gegebenen Form alle für einen RequestParameter
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
	 * Diese Methode codiert alle in der requestParameters-Map enthaltenen Attribute in eine für
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
	 * Diese Methode liefert den Link zu einer gewünschten Website
	 * 		@param domTree, hoster, linkID
	 *  	@return action
	 */
	protected String getLink(Document domTree, String hoster, String linkID) {
		String action = new String();
		NodeList nl = domTree.getElementsByTagName("a");
		Iterator<Node> it = new NodeListIterator(nl);
		Pattern p = Pattern.compile(linkID);
		
		while(it.hasNext()) {
			Node current = it.next(); 
			Node href = current.getAttributes().getNamedItem("href");
			if((href != null) && (p.matcher(href.getNodeValue()).find())) {
				action = hoster + href.getNodeValue();
			}
		}
		
		return action;
	}
	
	/*
	 * Diese Methode führt einen Post - Request aus mit Übergabe von Parametern
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
	 * Diese Methode führt einen Request aus ohne übergabe von Parametern
	 * 		@param	action
	 * 		@return getMethodResponse
	 */
	protected URLConnection request(String action) throws IOException {
		URL url = new URL(action);
		URLConnection getMethodResponse = url.openConnection();
		
		return getMethodResponse;
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
	
	/*
	 * Diese Methode ergänzt den fehlenden Wert des Attributs Captcha
	 * 		@param postRequestParameters, captchaCode
	 */
	protected void addCaptcha(Map<String,String> postRequestParameters, String captchaCode) {
		Set<Map.Entry<String,String>> set = postRequestParameters.entrySet();
		Iterator<Map.Entry<String,String>> it = set.iterator();
		while(it.hasNext()) {
			Map.Entry<String,String> current = it.next();
			if((current.getValue().equals("")) && ((current.getKey().indexOf("capt") > -1) || (current.getKey().indexOf("image") > -1)))   {
				postRequestParameters.put(current.getKey(), captchaCode);
			}
		}
	}
	
	/*
	 * Diese Methode ermittelt alle Parameter die für einen Request erforderlich sind und setzt die fehlenden Werte.
	 * 		@param postRequestPage, hoster, imageID
	 * 		@return encodedParameters
	 */
	
	protected String requestPrepare(InputStream postRequestPage, String hoster, String imageID) throws IOException {
		if(imageID == null) {
			imageID = new String();
		}
		
		Tidy tidy = new Tidy();
		Document requestPageDOM = tidy.parseDOM(postRequestPage, null);
		NodeList formNodes = requestPageDOM.getElementsByTagName("form");
		Node form = formNodes.item(0);
		Map<String,String> requestParameters = new HashMap<String,String>();
		getRequestParameters(form, /*&*/requestParameters);
		String imageSource;	
		if((imageSource = getCaptchaImage(requestPageDOM, imageID)) != null) {
			InputStream imageStream = request(hoster + imageSource).getInputStream();
			BufferedImage captchaImage = (BufferedImage)new ImageIcon(ImageIO.read(imageStream)).getImage();
			new AwtImage(captchaImage);
			BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
			String captcha = consoleReader.readLine();
			addCaptcha(/*&*/requestParameters, captcha);
		}
		String encodedParameters = encodeParamters(requestParameters);
		
		return encodedParameters;
	}
	
	/*
	 * Diese Methode führt alle erforderlichen Schritte durch, die zur Seite des PostRequest führen
	 *  	@param url
	 *  	@return postRequestPage 
	 */
	protected URLConnection getRequestPage(URL url) throws IOException{
		return null;
	}
	
	
}
