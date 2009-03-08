package download.hoster;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import parser.Tag;
import web.Connection;
import download.Download;

public class Uploaded extends Download {
	public Uploaded(URL url, File destination) {
		super(url, destination);
		this.setFileName(this.detectFileName(url));
		this.setExpectedSize(this.detectFileSize(url));
	}

	private String detectFileName(URL url) {
		try {
			Connection webConnection = new Connection();
			webConnection.connect(url);
			Tag document = webConnection.getDocument();
			Tag table = document.getElementsByClass("table", "inputActive")
					.get(0);
			List<Tag> tableRows = table.getComplexTag("tr");
			List<String> foo = new ArrayList<String>();
			for (Tag tableRow : tableRows) {
				List<Tag> tableCells = tableRow.getComplexTag("td");
				String value = tableCells.get(1).toString().replaceAll(
						"<[^>]+>|\\s+", "");
				foo.add(value);
			}
			return foo.get(0) + foo.get(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url.toString();
	}

	private long detectFileSize(URL url) {
		try {
			Connection webConnection = new Connection();
			webConnection.connect(url);
			Tag document = webConnection.getDocument();
			Tag table = document.getElementsByClass("table", "inputActive")
					.get(0);
			List<Tag> tableRows = table.getComplexTag("tr");
			List<String> foo = new ArrayList<String>();
			for (Tag tableRow : tableRows) {
				List<Tag> tableCells = tableRow.getComplexTag("td");
				String value = tableCells.get(1).toString().replaceAll(
						"<[^>]+>|\\s+", "");
				foo.add(value);
			}
			return (long)(Double.parseDouble(foo.get(2).replaceAll("KB", "")) * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void prepareDownload() throws ThreadDeath {
		try {
			throw new IOException();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}