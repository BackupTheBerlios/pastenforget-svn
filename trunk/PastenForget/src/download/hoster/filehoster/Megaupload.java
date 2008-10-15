package download.hoster.filehoster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import middleware.Tools;
import parser.FormProperties;
import parser.Request;
import parser.Tag;
import queue.Queue;
import stream.ServerDownload;
import download.Download;
import download.DownloadInterface;
import exception.CancelException;
import exception.StopException;

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
			Tag htmlDocument = Tools.getTagFromInputStream(is, false);
			System.out.println("Content-Length: "
					+ htmlDocument.toString().length());
			Tag image = htmlDocument.getSimpleTag("img").get(0);
			// TODO Fenster für Captchaeingabe
			String captcha = "http://www.megaupload.com"
					+ image.getAttribute("src");
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

			List<FormProperties> forms = htmlDocument.getFormulars();
			FormProperties requestForm = forms.get(0);
			requestForm.addParameter("imagestring", captchaCode);

			Request request = new Request(requestForm);
			is = request.post();

			htmlDocument = Tools.getTagFromInputStream(is, false);
			System.out.println("Content-Length: "
					+ htmlDocument.toString().length());
			String[] vars = { "", "", "" };
			int counter = 0;
			for (Tag var : htmlDocument.getJavascript()) {

				if (var.toString().matches("var[\\s\\w]{3}=.*")) {
					String regex = "[(-{'}]{1}[\\w]+[{'})-]{1}";
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(var.toString());
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
			for (Tag link : htmlDocument.getSimpleTag("a")) {
				if (link.toString().indexOf("+") != -1) {
					cryptedLink = link.getAttribute("href");
				}
			}
			int pos = cryptedLink.indexOf("'");
			String front = cryptedLink.substring(0, pos);
			String back = cryptedLink.substring(pos + 13);
			System.out.println(cryptedLink);
			System.out.println(front);
			System.out.println(append);
			System.out.println(back);
			this.setDirectUrl(new URL(front + append + back));
			
			String filename = this.createRealFilename();
			this.setFileName(filename);

			System.out.println(this.getDirectUrl().toString());
			
			int waitingTime = 46;
			if (this.isStopped()) {
				throw new StopException();
			}
			if (this.isCanceled()) {
				throw new CancelException();
			}
			this.wait(waitingTime);
			ServerDownload.download(this);

		} catch (IOException io) {
			io.printStackTrace();
		} catch (StopException stopped) {
			System.out.println("Download stopped: " + this.getFileName());
		} catch (CancelException canceled) {
			System.out.println("Download canceled: " + this.getFileName());
		}

	}

}
