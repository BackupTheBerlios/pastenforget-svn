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
	private File downloadDirectory = new File(Tools.getProgramPath()
			.getAbsolutePath());

	private File srcDirectory = new File(Tools.getProgramPath()
			.getAbsolutePath());

	private int userInterface = 0;

	private String language = "English";

	private final File settingsFile = new File(Tools.getProgramPath()
			.getAbsolutePath()
			+ "/pnf-settings.xml");

	private Document dom;

	public Settings() {
		this.restore();
	}

	public void setDownloadDirectory(File downloadDirectory) {
		if (!"".equals(downloadDirectory.getPath())
				&& (downloadDirectory != null)) {
			this.downloadDirectory = downloadDirectory;
		}
	}

	public File getDownloadDirectory() {
		return this.downloadDirectory;
	}

	public void setSrcDirectory(File srcDirectory) {
		if (!"".equals(srcDirectory.getPath()) && (srcDirectory != null)) {
			this.srcDirectory = srcDirectory;
		}
	}

	public File getSrcDirectory() {
		return srcDirectory;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		for (String lang : Languages.getLanguages()) {
			if (lang.equals(language)) {
				this.language = language;
				return;
			}
		}
		this.language = "English";
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

		nextElement = dom.createElement(SettingsEnum.SRCDIR.getName());
		nextElement
				.setTextContent(getSrcDirectory() != null ? getSrcDirectory()
						.getPath() : "");
		rootElement.appendChild(nextElement);

		nextElement = dom.createElement(SettingsEnum.LANGUAGE.getName());
		nextElement.setTextContent(getLanguage());
		rootElement.appendChild(nextElement);

		try {
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
					settingsFile), format);

			serializer.serialize(dom);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean restore() {
		if (settingsFile.exists()) {
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

			actNodes = rootElement.getElementsByTagName(SettingsEnum.SRCDIR
					.getName());
			if (actNodes.getLength() >= 1) {
				setSrcDirectory(new File(actNodes.item(0).getTextContent()));
			}

			actNodes = rootElement.getElementsByTagName(SettingsEnum.LANGUAGE
					.getName());
			if (actNodes.getLength() >= 1) {
				setLanguage(actNodes.item(0).getTextContent());
			}
			System.out.println("Load settings: done");
			return true;
		} else {
			System.out.println("Load settings: no file");
			return false;
		}
	}

}
