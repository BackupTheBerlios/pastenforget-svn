package download.hoster.filehoster;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import middleware.Tools;
import parser.FormProperties;
import parser.Request;
import parser.Tag;
import queue.Queue;
import stream.ServerDownload;
import download.Download;
import download.DownloadInterface;
import download.Status;
import exception.CancelException;
import exception.RestartException;
import exception.StopException;

public class Megaupload extends Download implements DownloadInterface {

	public Megaupload() {
		super();
	}

	@Override
	public void setInformation(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus(Status.getWaiting());
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
		String fileName = new String();
		while (m.find()) {
			fileName = m.group();
		}
		return Tools.createWellFormattedFileName(fileName);
	}

	@Override
	public void run() {
		URL url = this.getUrl();
		try {
			InputStream is = url.openConnection().getInputStream();
			Tag htmlDocument = Tools.createTagFromWebSource(is, false);
			System.out.println("Content-Length: "
					+ htmlDocument.toString().length());
			Tag image = htmlDocument.getSimpleTag("img").get(0);
			String captcha = "http://www.megaupload.com"
					+ image.getAttribute("src");
			Image captchaImage = ImageIO.read(new URL(captcha));
			this.setCaptcha(captchaImage);
			String captchaCode = new String();
			do {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				captchaCode = this.getCaptchaCode();
			} while (captchaCode.equals(""));
			if (captchaCode.equals("cancel")) {
				this.setStatus(Status.getStopped());
				throw new CancelException();
			} else if (captchaCode.equals("new")) {
				this.setCaptchaCode("");
				throw new RestartException();
			}

			List<FormProperties> forms = htmlDocument.getFormulars();
			FormProperties requestForm = forms.get(0);
			requestForm.addParameter("imagestring", captchaCode);

			Request request = new Request(requestForm);
			is = request.post();

			htmlDocument = Tools.createTagFromWebSource(is, false);
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
		} catch (RestartException restart) {
			this.run();
		} catch (StopException stopped) {
			System.out.println("Download stopped: " + this.getFileName());
		} catch (CancelException canceled) {
			System.out.println("Download canceled: " + this.getFileName());
		}

	}

}
