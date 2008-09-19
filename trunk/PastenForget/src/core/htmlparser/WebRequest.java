package core.htmlparser;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO
public class WebRequest {
	
	public static String createParameterString(WebForm form) {
		String paramStr;
		List<Object> attributeNames = form.getAttributeNames();
		paramStr = attributeNames.get(0) + "=" + URLEncoder.encode(form.getAttributeValue((String)attributeNames.get(0)));
		for(int i = 1; i < attributeNames.size(); i++) {
			paramStr += "&" + attributeNames.get(i) + "=" +URLEncoder.encode(form.getAttributeValue((String)attributeNames.get(i)));
		}
		
		return paramStr;
	}
	
	
	
	public static void main(String[] args) {
		try {
		String page = Utilities.downloadHTMLPage("http://rapidshare.com/files/138763490/Sonic_Syndicate-Love_And_Other_Disasters-_Advance_-2008-mediaportal_by_Jho.rar");
		HTMLParser parser = new HTMLParser(page);
		List<WebImage> images = parser.parseImage();
		String captchaLink = "http://www.megaupload.com" + images.get(0).getAttributeValue("src");
		
		
		// Utilities.downloadFile(captchaLink, "MegauploadCurrentCaptcha.jpg", "");
		
		String imagestring = Utilities.readCaptchacode();
		
		WebForm form = parser.parseForm().get(0);
		// form.get(0).writeTag();
		form.setCaptcha(imagestring);
		String action = form.getAction();
		List<Object> attributes = form.getAttributeNames();
		
		String paramStr = createParameterString(form);
		request(paramStr, action);
		
		} catch(Exception e) {}
	}
	
	
	public static void request(String paramStr, String action) {
		try {
			/*
			*Establish a connection to the application.
			*/
			URL url = new URL(action);
			URLConnection con = url.openConnection();
			String lengthString = String.valueOf(paramStr.length());
			/*
			* configure the connection to allow for the operations necessary.
			* 1. Turn off all caching, so that each new request/response is made
			* from a fresh connection with the servlet.
			* 2. Indicate that this client will attempt to SEND and READ data to/from the servlet.
			*/
			con.setUseCaches(false);
			con.setDefaultUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			// Set the content type we are POSTing and length of the data.
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Length", lengthString);
			
			OutputStream os = con.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			// send data to servlet
			osw.write(paramStr);
			osw.flush();
			osw.close();
			
			//collect the response code from the servlet
			/*
			int responseCode = -1;
			if(protocol.equalsIgnoreCase("HTTP")){
			responseCode = ((HttpURLConnection)con).getResponseCode();
			}
			else if(protocol.equalsIgnoreCase("HTTPS")){
			responseCode = ((com.sun.net.ssl.HttpsURLConnection)con).getResponseCode();
			}
			*/
			//System.out.println(responseCode);

			//Read the input from that application
			InputStream is = con.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			String page = new String();
			System.out.println("Response from servlet:\n");
			while ( (line = br.readLine()) != null) {
				System.out.println(line);
				page+= line;
			}
			br.close();
			isr.close();	
			System.out.println(page);
			/*
			HTMLParser parser = new HTMLParser(page);
			List<WebLink> links = parser.parseLink();
			
			for(int i = 0; i < links.size(); i++) {
				System.out.println(i + ": " + links.get(i).getAttributeValue("href"));
			}
			List<String> var = parser.parseJavaScriptVar();
			System.out.println(var.get(0));
			System.out.println(var.get(1));
			*/

			} catch (Throwable t) { 
			t.printStackTrace();
			}	
			}
}