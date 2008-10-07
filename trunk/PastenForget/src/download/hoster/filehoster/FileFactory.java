package download.hoster.filehoster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import parser.Parser;
import parser.Request;
import queue.Queue;
import download.Download;
import download.DownloadInterface;

public class FileFactory extends Download implements DownloadInterface {

	public FileFactory(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(createFilename());
	}

	public String createFilename() {
		return null;
	}

	@Override
	public void run() {
		try {
			URL url = this.getUrl();
			InputStream is = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(is, false);
			String basicLink = new String();
			for (String currentLink : Parser.getSimpleTag("a", page)) {
				if (currentLink.matches(".*dlf/.*")) {
					basicLink = Parser.getAttribute("href", currentLink);
				}
			}
			url = new URL("http://filefactory.com" + basicLink);
			URLConnection urlc = url.openConnection();
			Iterator<Map.Entry<String, List<String>>> header = urlc
					.getHeaderFields().entrySet().iterator();
			Map<String, String> headerMap = new HashMap<String, String>();
			while (header.hasNext()) {
				Map.Entry<String, List<String>> headerEntry = header.next();
				String key = headerEntry.getKey();
				String value = headerEntry.getValue().get(0);
				if (key != null) {
					headerMap.put(key, value);
				}
			}

			is = urlc.getInputStream();
			page = Parser.convertStreamToString(is, false);
			String iframeLink = new String();
			for (String currentIframe : Parser.getSimpleTag("iframe", page)) {
				if (currentIframe.indexOf("/check") != -1) {
					iframeLink = Parser.getAttribute("src", currentIframe);
				}
			}

			url = new URL("http://filefactory.com/check"
					+ iframeLink.replaceAll("amp;", ""));
			is = url.openConnection().getInputStream();

			url = new URL("http://filefactory.com/check"
					+ iframeLink.replaceAll("amp;", ""));
			urlc = url.openConnection();
			is = urlc.getInputStream();
			page = Parser.convertStreamToString(is, true);

			String requestForm = Parser.getComplexTag("form", page).get(0);
			Request request = new Request();

			String image = Parser.getSimpleTag("img", page).get(0);
			String captcha = "http://filefactory.com"
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

			request.setAction("http://filefactory.com/check/check/"
					+ iframeLink.replaceAll("amp;", ""));

			List<String> input = Parser.getSimpleTag("input", requestForm);
			Iterator<String> inputIt = input.iterator();
			while (inputIt.hasNext()) {
				String currentInput = inputIt.next();
				String name = new String();
				String value = new String();
				if ((name = Parser.getAttribute("name", currentInput)) != null) {
					value = Parser.getAttribute("value", currentInput);
					if (value == null) {
						request.addParameter(name, captchaCode);
					} else {
						request.addParameter(name, value);
					}
				}
			}

			is = request.request();
			page = Parser.convertStreamToString(is, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * public static void main(String[] args) throws Exception { FileFactory ff =
	 * new FileFactory(new URL(
	 * "http://filefactory.com/file/25f11d/n/pb403_part4_rar"), null, null);
	 * ff.run(); }
	 */
}
