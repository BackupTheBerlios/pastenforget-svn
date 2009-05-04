package download.hoster;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.Formular;
import parser.Tag;
import web.Connection;
import download.Download;

public class BlueHost extends Download {

	public BlueHost(URL url, File destination) {
		super(url, destination);
		this.setFileName(this.detectFileName(url));
		this.setExpectedSize(0);
	}

	private String detectFileName(URL url) {
		String regex = ".*/(.*)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url.toString());
		String fileName = "";
		while (m.find()) {
			fileName = m.group(1);
		}
		System.out.println("FileName: " + fileName);
		return fileName;
	}

	/*
	 * private long detectFileSize(URL url) { Connection webConnection = new
	 * Connection(); try { webConnection.connect(url); Tag document =
	 * webConnection.getDocument(); Tag paragraph =
	 * document.getElementsByClass("p", "downloadlink").get(0); String fileSize =
	 * paragraph.getComplexTag("font").get(0).toString().replaceAll("<[^>]+>",
	 * ""); String regex = "[0-9]+"; Pattern p = Pattern.compile(regex); Matcher
	 * m = p.matcher(fileSize); m.find(); return Long.parseLong(m.group()) *
	 * 1024; } catch(IOException e) { e.printStackTrace(); } return 0; }
	 */

	@Override
	public void prepareDownload() throws ThreadDeath {
		HosterUtilities util = new HosterUtilities(this);
		try {
			Connection webConnection = new Connection();
			webConnection.connect(this.getUrl());
			webConnection.getDocument(false);
			System.out.println("wait...");
			util.waitSeconds(2);
			webConnection.connect(this.getUrl());
			Tag document = webConnection.getDocument(false);
			Tag form = document.getComplexTag("form").get(2);
			System.out.println(form.toString());
			Formular formular = new Formular(form);
			String action = "http://bluehost.to" + formular.getAction();
			webConnection.connect(action);
			webConnection.doPost(formular.getPostParameters());
			InputStream iStream = webConnection.getInputStream();
			util.download(iStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		String link = "http://bluehost.to/file/4uaE0PKIT/In.3.Tagen.bist.du.tot.2.German.2008.AC3.DVDRiP.XviD-HACO.part09.rar";
										 
		BlueHost bh = new BlueHost(new URL(link),
				new File("/home/christopher/"));
		bh.start();
	}

}
