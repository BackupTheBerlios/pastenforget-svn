package settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import middleware.Tools;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * In diesen Object werden alle Einstellungen verwaltet. Alle Einstellung
 * koennen abgerufen oder gespeichert werden.
 * 
 * @author executor
 * 
 */

public class Settings {
	private File downloadDirectory = Tools.getProgramPath();

	private File ddlDirectory = Tools.getProgramPath();

	private int userInterface = 0;

	private int language = 0;

	private final String settingsFile = Tools.getProgramPath()
			+ "/pnf-settings.xml";

	private Document dom;

	public Settings() {
		this.restore();
	}

	public void setDownloadDirectory(File downloadDirectory) {
		if (!"".equals(downloadDirectory.getPath()) && (downloadDirectory != null)) {
			this.downloadDirectory = downloadDirectory;
		}
	}

	public File getDownloadDirectory() {
		return this.downloadDirectory;
	}

	public void setDdlDirectory(File ddlDirectory) {
		if (!"".equals(ddlDirectory.getPath()) && (ddlDirectory != null)) {
			this.ddlDirectory = ddlDirectory;
		}
	}

	public File getDdlDirectory() {
		return ddlDirectory;
	}

	public void setUserInterface(int userInterface) {
		for (LookAndFeelEnum enu : LookAndFeelEnum.values()) {
			if (enu.getKey() == userInterface) {
				this.userInterface = userInterface;
				return;
			}
		}
		this.userInterface = LookAndFeelEnum.STANDARD.getKey();
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		for (LanguageEnum enu : LanguageEnum.values()) {
			if (enu.getKey() == language) {
				this.language = enu.getKey();
				return;
			}
		}
		this.language = LanguageEnum.STANDARD.getKey();
	}

	public int getUserInterface() {
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

		Element rootElement = dom
				.createElement(SettingsEnum.SETTINGS.getName());
		dom.appendChild(rootElement);

		Element nextElement = dom.createElement(SettingsEnum.LOOKANDFEEL
				.getName());
		nextElement.setTextContent(Integer.toString(getUserInterface()));
		rootElement.appendChild(nextElement);

		nextElement = dom.createElement(SettingsEnum.DOWNLOADDIR.getName());
		nextElement
				.setTextContent(getDownloadDirectory() != null ? getDownloadDirectory()
						.getPath()
						: "");
		rootElement.appendChild(nextElement);

		nextElement = dom.createElement(SettingsEnum.DDLDIR.getName());
		nextElement
				.setTextContent(getDdlDirectory() != null ? getDdlDirectory()
						.getPath() : "");
		rootElement.appendChild(nextElement);

		nextElement = dom.createElement(SettingsEnum.LANGUAGE.getName());
		nextElement.setTextContent(Integer.toString(getLanguage()));
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
			actNodes = rootElement
					.getElementsByTagName(SettingsEnum.LOOKANDFEEL.getName());
			if (actNodes.getLength() >= 1) {
				setUserInterface(Integer.parseInt(actNodes.item(0)
						.getTextContent()));
			}

			actNodes = rootElement
					.getElementsByTagName(SettingsEnum.DOWNLOADDIR.getName());
			if (actNodes.getLength() >= 1) {
				setDownloadDirectory(new File(actNodes.item(0).getTextContent()));
			}

			actNodes = rootElement.getElementsByTagName(SettingsEnum.DDLDIR
					.getName());
			if (actNodes.getLength() >= 1) {
				setDdlDirectory(new File(actNodes.item(0).getTextContent()));
			}

			actNodes = rootElement.getElementsByTagName(SettingsEnum.LANGUAGE
					.getName());
			if (actNodes.getLength() >= 1) {
				setLanguage(Integer.parseInt(actNodes.item(0).getTextContent()));
			}
			System.out.println("Load settings: done");
			return true;
		} else {
			System.out.println("Load settings: no file");
			return false;
		}
	}

}
