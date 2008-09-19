package core.hoster;

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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

/**
 * Extrahiert Directlinks und fuehrt den Download aus. Hoster: rapidshare.com
 * 
 * @author cschaedl
 */

//public class Rapidshare extends Download implements DownloadInterface {
public class Rapidshare {
	URLConnection ttt;
	
	
	private static String getAction(Node node) {
		return node.getAttributes().getNamedItem("action").getNodeValue();
	}	
	
	public static void getRequestParameters(Node node, Map<String, String> requestParameters) {
		if(node.hasChildNodes()) {
			Iterator<Node> it = new NodeListIterator(node.getChildNodes());
			while(it.hasNext()) {
	    		Node current = it.next();
	    		Node name = current.getAttributes().getNamedItem("name");
				Node value = current.getAttributes().getNamedItem("value");
				if(name != null) {
					String val = (value != null) ? value.getNodeValue() : "";
					requestParameters.put(name.getNodeValue(), val);
				}
				getRequestParameters(current, requestParameters);
	    	}	
	    }
	}
	
	public static String encodeParameters(Map<String,String> requestParameters) throws UnsupportedEncodingException {
		Set<Map.Entry<String,String>> set = requestParameters.entrySet();
		Iterator<Map.Entry<String,String>> it = set.iterator();
		String param = new String();
		
		while(it.hasNext()) {
			Map.Entry<String,String> current = it.next();
			param += "&" + current.getKey() + "=" + URLEncoder.encode(current.getValue(), "iso-8859-1");
		}
		
		return param.substring(1);
	}
	
	public static void addCaptcha(Map<String,String> requestParameters, String captcha) {
		Set<Map.Entry<String,String>> set = requestParameters.entrySet();
		Iterator<Map.Entry<String,String>> it = set.iterator();
		while(it.hasNext()) {
			Map.Entry<String,String> current = it.next();
			if((current.getValue().equals("")) && ((current.getKey().indexOf("capt") > -1) || (current.getKey().indexOf("image") > -1)))   {
				requestParameters.put(current.getKey(), captcha);
			}
		}
	}
	
	public static String getCaptchaImage(Document node, String identifier) {
		NodeList imgNodes = node.getElementsByTagName("img");
		for(int i = 0; i < imgNodes.getLength() ; i++) {
			Node src = imgNodes.item(i).getAttributes().getNamedItem("src");
			if(src != null) {
				if(src.getNodeValue().indexOf(identifier)>-1) {
					return src.getNodeValue();
				}
			}
		}
		return null;
	}
	
	public static void reportHeader(URLConnection urlc) {
		Iterator<Map.Entry<String,List<String>>> it = urlc.getHeaderFields().entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String,List<String>> current = it.next();
			current.getKey();
			Iterator<String> it2 = current.getValue().iterator();
			while(it2.hasNext()) {
				String current2 = it2.next();
				System.out.println(current.getKey() + " : " + current2);
			}
		}
	}
	
	
	public static String getLink(String hosterURL, Document node, String identifier) {
		String action = new String();
		NodeList nl = node.getElementsByTagName("a");
		Iterator<Node> it = new NodeListIterator(nl);
		Pattern p = Pattern.compile(identifier);
		
		while(it.hasNext()) {
			Node current = it.next(); 
			Node href = current.getAttributes().getNamedItem("href");
			if((href != null) && (p.matcher(href.getNodeValue()).matches())) {
				action = href.getNodeValue();
			}
		}
		
		return hosterURL + action;
	}
	

	/*
	 * Für Netload.in
	 */
	
	private static CookieManager cm;
	
	public static InputStream getRequestSite(String link) throws MalformedURLException, IOException {
		cm = new CookieManager();
		URL url = new URL(link);
		URLConnection urlc = url.openConnection();
		cm.storeCookies(urlc);
		
		InputStream in = urlc.getInputStream();
		
		Tidy tidy = new Tidy(); 
		tidy.setShowWarnings(false);
		
		Document node = tidy.parseDOM(in, null);
		
		String action = getLink("http://netload.in/", node, "[^h]{1}.*&.*");
		url = new URL(action);
		urlc = url.openConnection();
		cm.setCookies(urlc);
		in = urlc.getInputStream();
		
		
		return in;
	}
	
	
	
	/*
	 * Für Megaupload.com und Rapidshare.com
	 */
	/*
	public static InputStream getRequestSite(String link) throws MalformedURLException, IOException {
		URL url = new URL(link);
		URLConnection urlc = url.openConnection();
		InputStream in = urlc.getInputStream();
		
		return in;
	}
	*/
	
	public static void main(String[] args) throws Exception {


	
		//URL url = new URL("http://www.megaupload.com/de/?d=E0HU2K66");
		
		String[] testLinks = {
				"http://www.megaupload.com/de/?d=E0HU2K66",
				"http://netload.in/datei8ff792b1165cc2dd5b4547702b8383ef.htm",
				""
		};
		
		
		InputStream in = getRequestSite(testLinks[1]);
		
		
		Tidy tidy = new Tidy(); 
		tidy.setShowWarnings(false);
		
		Map<String, String> requestParameters = new HashMap<String,String>();
		
		Document node = tidy.parseDOM( in, null );
		NodeList formNodes = node.getElementsByTagName("form");
		Node form = formNodes.item(0);
		
		
		String action = getAction(form);
		getRequestParameters(form, requestParameters);
		requestParameters.put("start", "1");
		String imageURL;
		if((imageURL = getCaptchaImage(node, "cap")) != null) {
			InputStream is = request("http://netload.in/" + imageURL);
			BufferedImage img = (BufferedImage)new ImageIcon(ImageIO.read(is)).getImage();
			new AwtImage(img);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String captcha = br.readLine();
			addCaptcha(requestParameters, captcha);
		}
		
		String paramStr = encodeParameters(requestParameters);
		System.out.println(paramStr);
		System.out.println(action);
		InputStream is = request(paramStr, "http://netload.in/" + action);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		
		
		
		while((line = br.readLine()) != null) {
			System.out.println(line);
		}
		
		
	}
	
	public static InputStream request(String action) throws IOException {
		URL url = new URL(action);
		URLConnection connection = url.openConnection();
		
		InputStream is = connection.getInputStream();

		return is;
	}
	
	public static InputStream request(String paramStr, String action) throws IOException {
			URL url = new URL(action);
			URLConnection connection = url.openConnection();
			String length = String.valueOf(paramStr.length());

			connection.setUseCaches(true);
			connection.setDefaultUseCaches(true);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", length);
			
			OutputStream os = connection.getOutputStream();
			OutputStreamWriter requestWriter = new OutputStreamWriter(os);
			requestWriter.write(paramStr);
			requestWriter.flush();
			requestWriter.close();
			
			
			Iterator<Map.Entry<String, List<String>>> it = connection.getHeaderFields().entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, List<String>> current = it.next();
				Iterator<String> it2 = current.getValue().iterator();
				while(it2.hasNext()) {
					System.out.println(current.getKey() + " : " + it2.next());
				}
			}
			
			
			InputStream is = connection.getInputStream();
			
			return is;
	}
	

}