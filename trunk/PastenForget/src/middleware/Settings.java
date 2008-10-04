package middleware;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class Settings {
	private File downloadDirectory = null;

	private File ddlDirectory = null;

	private short userInterface = 1;

	private final String settingsFile = "pnf-settings.xml";

	private Document dom;

	public Settings() {
		//getJarDirectory();
		this.restore();
	}

	public void setDownloadDirectory(File downloadDirectory) {
		this.downloadDirectory = downloadDirectory;
	}

	public File getDownloadDirectory() {
		return this.downloadDirectory;
	}

	public void setDdlDirectory(File ddlDirectory) {
		this.ddlDirectory = ddlDirectory;
	}

	public File getDdlDirectory() {
		return ddlDirectory;
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		Element rootElement = dom.createElement("Settings");
		dom.appendChild(rootElement);

		Element nextElement = dom.createElement("LookAndFeel");
		nextElement.setTextContent(Short.toString(getUserInterface()));
		rootElement.appendChild(nextElement);

		nextElement = dom.createElement("DownloadDirectory");
		nextElement
				.setTextContent(getDownloadDirectory() != null ? getDownloadDirectory()
						.getPath()
						: "");
		rootElement.appendChild(nextElement);

		nextElement = dom.createElement("DdlDirectory");
		nextElement
				.setTextContent(getDdlDirectory() != null ? getDdlDirectory()
						.getPath() : "");
		rootElement.appendChild(nextElement);

		try {
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
					new File(settingsFile)), format);

			serializer.serialize(dom);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean restore() {
		if (new File(settingsFile).exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				dom = db.parse(settingsFile);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Element rootElement = dom.getDocumentElement();
			NodeList actNodes;
			actNodes = rootElement.getElementsByTagName("LookAndFeel");
			if (actNodes.getLength() >= 1) {
				setUserInterface(Short.parseShort(actNodes.item(0)
						.getTextContent()));
			}
			actNodes = rootElement.getElementsByTagName("DownloadDirectory");
			if (actNodes.getLength() >= 1) {
				setDownloadDirectory(new File(actNodes.item(0).getTextContent()));
			}
			actNodes = rootElement.getElementsByTagName("DdlDirectory");
			if (actNodes.getLength() >= 1) {
				setDdlDirectory(new File(actNodes.item(0).getTextContent()));
			}
			System.out.println("Load settings: done");
			return true;
		} else {
			System.out.println("Load settings: no file");
			return false;
		}
	}
	
	// TODO Ordner des Programms festellen, um settings dort abzuspeichern.
	// Zur Zeit haut der die Dateien irgendwo hin, wenn kein absoluter Pfad.
	// Oder andere Lösung finden.
	public File getJarDirectory() {
		String name = this.getClass().getName();
		return new File("test");
	}

}
