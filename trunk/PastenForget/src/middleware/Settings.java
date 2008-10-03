package middleware;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class Settings {
	private File destination = null;
	
	private File destinationDllwarez = null;
	
	private short userInterface = 1;
	
	private final String settingsFile = "settings.xml";
	
	Document dom;
	
	public Settings () {
		this.restore();
	}
	
	private boolean restore() {
		if (new File(settingsFile).exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				dom = db.parse(settingsFile);
			}catch(Exception e) { e.printStackTrace();}
			
			Element rootElement = dom.getDocumentElement();
			NodeList actNodes;
			actNodes = rootElement.getElementsByTagName("LookAndFeel");
			if (actNodes.getLength() >= 1) this.userInterface = Short.parseShort(actNodes.item(0).getTextContent());
			actNodes = rootElement.getElementsByTagName("DownloadDirectory");
			if (actNodes.getLength() >= 1) this.destination = new File (actNodes.item(0).getTextContent());
			actNodes = rootElement.getElementsByTagName("DownloadDirectory");
			if (actNodes.getLength() >= 1) this.destinationDllwarez = new File (actNodes.item(0).getTextContent());
		}else {
			this.userInterface = 1;
			this.destination = null;
			this.destinationDllwarez = null;
		}
			
		
		return false;
	}

	public void setDestination(File destination) {
		this.destination = destination;
	}
	
	public File getDestination() {
		return this.destination;
	}
	
	public void setDestinationDllwarez(File destinatioDllwarez) {
		this.destinationDllwarez = destinatioDllwarez;
	}

	public File getDestinationDllwarez() {
		return destinationDllwarez;
	}
	
    public void setUserInterface(short userInterface) {
		this.userInterface = userInterface;
	}

	public short getUserInterface() {
		return userInterface;
	}

	public void save() { 
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();
		}catch(Exception e) { e.printStackTrace();}
		
		Element rootElement = dom.createElement("Settings");
		dom.appendChild(rootElement);
		
		Element nextElement = dom.createElement("LookAndFeel");
		nextElement.setTextContent(Short.toString(this.userInterface));
		rootElement.appendChild(nextElement);
		
		nextElement = dom.createElement("DownloadDirectory");
		nextElement.setTextContent(this.destination != null ? this.destination.getPath() : "");
		rootElement.appendChild(nextElement);
		
		nextElement = dom.createElement("DllDirectory");
		nextElement.setTextContent(this.destinationDllwarez != null ? this.destinationDllwarez.getPath() : "");
		rootElement.appendChild(nextElement);
		
		try
		{
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			XMLSerializer serializer = new XMLSerializer(
			new FileOutputStream(new File(settingsFile)), format);
			
			serializer.serialize(dom);
		} catch(IOException e) { e.printStackTrace();}
    } 
	
    public void read() {
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse("settings.xml");
		}catch(Exception e) { e.printStackTrace();}
		
		Element rootElement = dom.getDocumentElement();
		
		System.out.println(rootElement.getAttribute("LookAndFeel"));
    }
    
    public static void main(String[] args) {
    	Settings settings = new Settings();
    	settings.setDestination(new File("/home/christopher/share"));
    	settings.save();
    	settings = new Settings();
    	settings.read();
    	System.out.println(settings.getDestination().toString());
    }
}
