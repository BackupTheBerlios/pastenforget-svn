package download.hoster.filehoster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.Parser;
import parser.Request;
import queue.Queue;
import stream.ServerDownload;
import download.Download;
import download.DownloadInterface;

public class Megaupload extends Download implements DownloadInterface {

	public Megaupload(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.createFilename());
	}

	public String createFilename() {
		String file = this.getUrl().getFile();
		String regex = "[^/]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(file);
		String filename = new String();
		while (m.find()) {
			filename = m.group();
		}
		return filename.replace("?d=", "");
	}

	public String createRealFilename() {
		String file = this.getDirectUrl().getFile();
		String regex = "[^/]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(file);
		String filename = new String();
		while (m.find()) {
			filename = m.group();
		}
		return filename;
	}

	@Override
	public void run() {
		URL url = this.getUrl();
		try {
			InputStream is = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(is, false);
			System.out.println("Content-Length: " + page.length());
			String image = Parser.getSimpleTag("img", page).get(0);
			// TODO Fenster für Captchaeingabe
			String captcha = "http://www.megaupload.com"
					+ Parser.getAttribute("src", image);
			is = new URL(captcha).openConnection().getInputStream();
			OutputStream os = new FileOutputStream("megaupload_captcha.img");
			byte[] buffer = new byte[1024];
			int receivedBytes;
			while ((receivedBytes = is.read(buffer)) != -1) {
				os.write(buffer, 0, receivedBytes);
			}
			this.setStatus("Captcha-Eingabe");
			System.out.println("Bitte geben Sie den Captcha Code ein!");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String captchaCode = br.readLine();

			String requestForm = Parser.getComplexTag("form", page).get(0);
			String action = Parser.getAttribute("action", requestForm);
			Request request = Parser.readRequestFormular(requestForm);
			request.setAction(action);
			request.addParameter("imagestring", captchaCode);
			is = request.request();
			page = Parser.convertStreamToString(is, false);
			System.out.println("Content-Length: " + page.length());
			String[] vars = { "", "", "" };
			int counter = 0;
			for (String var : Parser.getJavaScript("var", page)) {

				if (var.matches("var[\\s\\w]{3}=.*")) {
					String regex = "[(-{'}]{1}[\\w]+[{'})-]{1}";
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(var);
					while (m.find()) {
						vars[counter++] = m.group().replaceAll("[^\\w]+", "");
					}

				}
			}
			char abs = (char) Integer.valueOf(vars[0]).intValue();
			char sqrt = (char) Math.sqrt(Double.valueOf(vars[2]));
			String append = vars[1] + String.valueOf(sqrt)
					+ String.valueOf(abs);

			String cryptedLink = new String();
			for (String current : Parser.getSimpleTag("a", page)) {
				if (current.indexOf("+") != -1) {
					cryptedLink = Parser.getAttribute("href", current);
				}
			}
			int pos = cryptedLink.indexOf("'");
			String front = cryptedLink.substring(0, pos);
			String back = cryptedLink.substring(pos + 13);
			this.setDirectUrl(new URL(front + append + back));
			String filename = this.createRealFilename();
			this.setFileName(filename);

			int waitingTime = 46;
			this.isStopped();
			this.isCanceled();
			this.wait(waitingTime);
			ServerDownload.download(this);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
