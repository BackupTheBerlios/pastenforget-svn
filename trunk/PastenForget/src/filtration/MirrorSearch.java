package filtration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import parser.Parser;
import parser.Request;

public class MirrorSearch {

	public static boolean search(String keyword, File destination)
			throws Exception {
		String[] genres = { "apps", "movies", "games", "series", "mp3s", "xxx",
				"hoers", "cracks" };
		System.out.println("gefundene Einträge");
		List<URL> foundEntries = new ArrayList<URL>();
		for (int i = 0; i < genres.length; i++) {
			foundEntries.addAll(searchInCategory(keyword, genres[i]));
		}

		for (URL url : foundEntries) {
			filterMirrors(url, destination);
		}

		if (foundEntries.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public static List<URL> searchInCategory(String search, String genre)
			throws Exception {
		URL url = new URL("http://ddl-warez.org/search.php?q="
				+ URLEncoder.encode(search, "UTF-8") + "&cat=" + genre);

		InputStream in = url.openConnection().getInputStream();
		String page = Parser.convertStreamToString(in);

		page = Parser.getComplexTag("div id=\"main_content\"", page).get(0);

		List<URL> foundEntries = new ArrayList<URL>();
		List<String> links = Parser.getComplexTag("a", page);
		Iterator<String> linkIt = links.iterator();
		while (linkIt.hasNext()) {
			String current = linkIt.next();
			foundEntries.add(new URL("http://ddl-warez.org/"
					+ Parser.getAttribute("href", current)));
		}

		return foundEntries;
	}

	public static boolean filterMirrors(URL url, File destination)
			throws Exception {
		System.out.println(url);
		if (url.toString().matches("http://[w]*ddl-warez.org/detail.php.*id=.*cat=.*")) {
			URLConnection urlc = url.openConnection();
			InputStream in = urlc.getInputStream();
			String page = Parser.convertStreamToString(in);

			String pwTable = page.substring(page.indexOf("Passwort:"));
			String password = Parser.getTagContent("td", Parser.getComplexTag(
					"td", pwTable).get(0));
			String table = Parser.getComplexTag("table", page).get(0);
			List<String> td = Parser.getComplexTag("td", table);
			String fileName = new String();
			for (String current : td) {
				String valign = Parser.getAttribute("valign", current);
				if ((valign != null) && (valign.equals("middle"))) {
					fileName = Parser.getTagContent("td", current).replaceAll(
							"<[^>]+>", "").replaceAll("\\t", "").replaceAll(
							"/", "");
				}
			}

			String local = destination.getPath() + File.separator + fileName
					+ "_pw_" + password + ".pnf";

			OutputStream os = new FileOutputStream(local);

			Iterator<String> formIt = Parser.getComplexTag("form", page)
					.iterator();
			while (formIt.hasNext()) {
				String currentForm = formIt.next();
				String action = "http://ddl-warez.org/"
						+ Parser.getAttribute("action", currentForm);
				String formName = Parser.getAttribute("name", currentForm);

				if (formName.indexOf("dl") != 0) {
					continue;
				}
				Request request = new Request(action);
				Iterator<String> inputIt = Parser.getSimpleTag("input",
						currentForm).iterator();
				while (inputIt.hasNext()) {
					String currentInput = inputIt.next();
					String name = Parser.getAttribute("name", currentInput);
					String value = Parser.getAttribute("value", currentInput);
					request.addParameter(name, value);
				}
				in = request.request();
				String singleLinkPage = Parser.convertStreamToString(in);

				List<String> frames = Parser.getSimpleTag("FRAME",
						singleLinkPage);
				for (String frame : frames) {
					if (frame.indexOf("http://rapidshare.com") > -1) {
						os.write((Parser.getAttribute("SRC", frame) + "\n")
								.getBytes());
					}
				}

			}
			return true;
		} else {
			return false;
		}

	}
}
