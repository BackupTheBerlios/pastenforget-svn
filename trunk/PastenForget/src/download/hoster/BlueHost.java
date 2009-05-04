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
			System.out.println("wait...60sec...");
			util.waitSeconds(60);
			System.out.println("waiting time over");
			webConnection.connect(action);
			webConnection.doPost(formular.getPostParameters());
			InputStream iStream = webConnection.getInputStream();
			util.download(iStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		String[] links = new String[] {
				"http://bluehost.to/file/jSbV8Mdkz/In.3.Tagen.bist.du.tot.2.German.2008.AC3.DVDRiP.XviD-HACO.part06.rar",
				"http://bluehost.to/file/DnYHyXwTl/In.3.Tagen.bist.du.tot.2.German.2008.AC3.DVDRiP.XviD-HACO.part07.rar",
				"http://bluehost.to/file/efZhinmKD/In.3.Tagen.bist.du.tot.2.German.2008.AC3.DVDRiP.XviD-HACO.part10.rar",
				"http://bluehost.to/file/L5J21xYZT/In.3.Tagen.bist.du.tot.2.German.2008.AC3.DVDRiP.XviD-HACO.part11.rar",
				"http://bluehost.to/file/xSOMcZY2e/In.3.Tagen.bist.du.tot.2.German.2008.AC3.DVDRiP.XviD-HACO.part12.rar",
				"http://bluehost.to/file/sD3oNIJm5/In.3.Tagen.bist.du.tot.2.German.2008.AC3.DVDRiP.XviD-HACO.part13.rar",
				"http://bluehost.to/file/Zs83SC0d9/In.3.Tagen.bist.du.tot.2.German.2008.AC3.DVDRiP.XviD-HACO.part14.rar",
				"http://bluehost.to/file/h346fdofR/In.3.Tagen.bist.du.tot.2.German.2008.AC3.DVDRiP.XviD-HACO.part15.rar"
		};
		for(String link : links) {								 
			BlueHost bh = new BlueHost(new URL(link), new File("/home/christopher/Desktop/"));
			bh.prepareDownload();
		}
	}

}
