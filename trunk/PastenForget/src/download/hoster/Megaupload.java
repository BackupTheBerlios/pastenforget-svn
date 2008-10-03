package download.hoster;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import queue.Queue;
import download.Download;
import download.DownloadInterface;
import parser.*;

public class Megaupload extends Download implements DownloadInterface {

	public Megaupload(URL url, Queue queue) {
		this.setUrl(url);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(url.toString());
	}

	public void createFilename() {
		// TODO
	}

	@Override
	public void run() {
		URL url = this.getUrl();
		try {
			InputStream is = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(is, true);
			String image = Parser.getSimpleTag("img", page).get(0);
			String captcha = "http://www.megaupload.com"
					+ Parser.getAttribute("src", image);
			is = new URL(captcha).openConnection().getInputStream();
			OutputStream os = new FileOutputStream("megaupload_captcha.img");
			byte[] buffer = new byte[1024];
			int receivedBytes;
			while ((receivedBytes = is.read(buffer)) != -1) {
				os.write(buffer, 0, receivedBytes);
			}
			System.out.println("Bitte geben Sie den Captcha Code ein!");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String captchaCode = br.readLine();

			String requestForm = Parser.getComplexTag("form", page).get(0);
			List<String> input = Parser.getSimpleTag("input", requestForm);
			Request request = new Request();
			String action = Parser.getAttribute("action", requestForm);
			request.setAction(action);
			Iterator<String> inputIt = input.iterator();
			while (inputIt.hasNext()) {
				String currentInput = inputIt.next();
				String name = new String();
				String value = new String();
				if ((name = Parser.getAttribute("name", currentInput)) != null) {
					value = Parser.getAttribute("value", currentInput);
					request.addParameter(name, value);
					System.out.println(name + " ,  " + value);
				}
			}
			request.addParameter("imagestring", captchaCode);
			is = request.request();
			page = Parser.convertStreamToString(is, true);
			System.out.println(page);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		Megaupload ml = new Megaupload(new URL(
				"http://www.megaupload.com/?d=OFRWQIRR"), null);
		ml.run();

	}

}
