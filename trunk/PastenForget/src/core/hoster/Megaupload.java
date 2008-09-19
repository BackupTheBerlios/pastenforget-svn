package core.hoster;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import core.Download;
import core.DownloadInterface;
import core.Queue;
import core.ServerConnection;
import core.ServerDownload;

public class Megaupload extends Download implements DownloadInterface {

	public Megaupload(URL url, Queue queue) {
		this.setUrl(url);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFilename("megaupload");
	}

	public String getDirectLink(String input) {
		return null;
	}

	public String getVar(String input) {
		Pattern p;
		Matcher m;
		String regex;
		String var = new String();
		String numbers[] = new String[3];
		String target;
		int i = 0;

		/*
		 * Filtert var b = String.fromCharCode(Math.abs(-99)); var e = 'b' +
		 * String.fromCharCode(Math.sqrt(2916)); aus HTML-Dokument
		 */

		regex = "var [\\w]{1} = .*[;]{1}";
		p = Pattern.compile(regex);
		m = p.matcher(input);

		while (m.find()) {
			var += input.substring(m.start(), m.end());
		}

		/*
		 * Filtert 99, 2, 2916 aus: var b = String.fromCharCode(Math.abs(-99));
		 * var e = '2' + String.fromCharCode(Math.sqrt(2916));
		 */
		regex = "[(']{1}[-]*[\\w]+[)']";
		p = Pattern.compile(regex);
		m = p.matcher(var);

		while (m.find()) {
			numbers[i] = var.substring(m.start() + 1, m.end() - 1);
			i++;
		}

		/*
		 * Erstellt den 3 stelligen Zielstring '2' +
		 * String.fromCharCode(Math.sqrt(2916)) + String.fromCharCode(-99)
		 */

		target = numbers[1]
				+ String.valueOf((char) Math.sqrt(Double.valueOf(numbers[2])))
				+ String.valueOf((char) Math.abs(Double.valueOf(numbers[0])
						.doubleValue()));

		return target;
	}



	public void wait(String waitingtime) {
	}

	public void downloadFileFromHoster(String link) {

	}

}
