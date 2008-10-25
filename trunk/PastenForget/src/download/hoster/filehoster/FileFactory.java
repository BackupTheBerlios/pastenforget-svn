package download.hoster.filehoster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

import middleware.Tools;
import parser.FormProperties;
import parser.Request;
import parser.Tag;
import queue.Queue;
import download.Download;
import download.DownloadInterface;

public class FileFactory extends Download implements DownloadInterface {

	public FileFactory() {
		super();
	}

	@Override
	public void setInformation(URL url, File destination, Queue queue) {
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
			URLConnection urlc = url.openConnection();
			InputStream in = urlc.getInputStream();
			Tag htmlDocument = Tools.createTagFromWebSource(in, false);
			List<Tag> links = htmlDocument.getComplexTag("a");
			String action = new String();
			for (Tag link : links) {
				String href = link.getAttribute("href");
				if ((href != null) && (href.matches("/dlf/.*"))) {
					action = href;
					break;
				}
			}

			String[] fragments = action.split("/");
			int index = 0;
			for (int i = 0; i < fragments.length; i++) {
				if (fragments[i].equals("f")) {
					index = i;
					break;
				}
			}
			String parameters = fragments[index] + "="
					+ URLEncoder.encode(fragments[index + 1], "UTF-8") + "&"
					+ fragments[index + 2] + "="
					+ URLEncoder.encode(fragments[index + 3], "UTF-8") + "&"
					+ fragments[index + 4] + "="
					+ URLEncoder.encode(fragments[index + 5], "UTF-8")
					+ "&reload=1";
			action = "http://www.filefactory.com/check/?" + parameters;

			Scanner scanner = new Scanner(new URL(action).openStream());
			while (scanner.hasNextLine()) {
				System.out.println(scanner.nextLine());
			}
			scanner.close();

			url = new URL(action);

			in = url.openConnection().getInputStream();
			htmlDocument = Tools.createTagFromWebSource(in, false);
			// String path = url.getPath();
			List<Tag> images = htmlDocument.getSimpleTag("img");
			String imageLink = new String();
			for (Tag image : images) {
				String source = image.getAttribute("src");
				if ((source != null) && (source.matches("/securimage/.*"))) {
					imageLink = source;
					break;
				}
			}
			url = new URL("http://filefactory.com"
					+ imageLink.replaceAll("&[^;]+;", ""));
			urlc = url.openConnection();

			// List<String> cookies =
			// urlc.getHeaderFields().get("Set-Cookie");
			// System.out.println(cookies.toString());
			in = urlc.getInputStream();

			OutputStream os = new FileOutputStream("captcha_filefactory");
			byte[] buffer = new byte[1024];
			int receivedBytes = 0;

			while ((receivedBytes = in.read(buffer)) > -1) {
				os.write(buffer, 0, receivedBytes);
			}
			os.flush();
			os.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String captchaCode = br.readLine();

			FormProperties properties = htmlDocument.getFormulars().get(0);
			properties.setAction("http://filefactory.com/check/");
			properties.addParameter("captcha", captchaCode);
			Request request = new Request(properties);
			in = request.get();

			htmlDocument = Tools.createTagFromWebSource(in, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
