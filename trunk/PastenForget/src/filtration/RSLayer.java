package filtration;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import middleware.Tools;
import parser.FormProperties;
import parser.Request;
import parser.Tag;

public class RSLayer {

	public List<String> filterLinks(URL url, File destination) throws Exception {
		InputStream is = url.openConnection().getInputStream();
		Tag htmlDocument = Tools.createTagFromWebSource(is, false);
		List<Tag> imgs = htmlDocument.getSimpleTag("img");
		String imageLink = new String();
		for (Tag img : imgs) {
			String source = img.getAttribute("src");
			if (source != null) {
				if (source.indexOf("captcha-") != -1) {
					imageLink = "http://rs-layer.com/" + source;
					break;
				}
			}
		}

		InputStream fis = new URL(imageLink).openStream();
		FileOutputStream fos = new FileOutputStream("captcha_rslayer.jpg");
		int len;
		byte[] buffer = new byte[1024];

		while ((len = fis.read(buffer)) != -1) {
			fos.write(buffer, 0, len);
		}

		//TODO Captchaeingabe-Fenster
		// Image captchaImage = ImageIO.read(new URL(imageLink));
		System.out.println("Bitte Captcha Code eingeben!");
		String captchaCode = new BufferedReader(
				new InputStreamReader(System.in)).readLine();

		FormProperties form = htmlDocument.getFormulars().get(0);
		form.addParameter("captcha_input", captchaCode);
		form.setAction(url.toString());
		Request request = new Request(form);
		is = request.post();
		htmlDocument = Tools.createTagFromWebSource(is, false);
		List<Tag> trs = htmlDocument.getSimpleTag("tr");
		List<URL> cryptedLinks = new ArrayList<URL>();
		for (Tag tr : trs) {
			String onClick = tr.getAttribute("onclick");
			if (onClick != null) {
				String id = onClick.split(";")[0].replace("getFile('", "")
						.replace("')", "");
				cryptedLinks.add(new URL("http://rs-layer.com/link-" + id
						+ ".html"));
			}
		}

		List<String> rsLinks = new ArrayList<String>();
		for (URL cryptedLink : cryptedLinks) {
			is = cryptedLink.openStream();
			htmlDocument = Tools.createTagFromWebSource(is, false);
			List<Tag> iframes = htmlDocument.getSimpleTag("iframe");
			for (Tag iframe : iframes) {
				String source = iframe.getAttribute("src");
				if (source != null) {
					StringBuffer sb = new StringBuffer();
					String[] asciiNumbers = source.replaceAll("&#", "").split(
							";");
					for (String asciiNumber : asciiNumbers) {
						char ascii = (char) Integer.valueOf(asciiNumber).intValue();
						sb.append(ascii);
					}
					rsLinks.add(sb.toString());
					System.out.println(sb.toString());
					
				}
			}
		}
		return rsLinks;
	}

}
