package filtration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import middleware.Tools;
import parser.FormProperties;
import parser.Request;
import parser.Tag;

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
		Tag htmlDocument = Tools.createTagFromWebSource(in, false);
		List<Tag> divs = htmlDocument.getComplexTag("div");
		Tag main_content = null;
		for (Tag div : divs) {
			if ("main_content".equals(div.getAttribute("id"))) {
				main_content = div;
				break;
			}
		}

		List<Tag> links = main_content.getComplexTag("a");
		List<URL> foundEntries = new ArrayList<URL>();
		for (Tag link : links) {
			foundEntries.add(new URL("http://ddl-warez.org/"
					+ link.getAttribute("href")));
		}

		return foundEntries;
	}

	public static boolean filterMirrors(URL url, File destination)
			throws Exception {
		String link = url.toString().replace("www.", "");
		if (link.matches("http://ddl-warez.org/detail.php.*id=.*cat=.*")) {
			URLConnection urlc = url.openConnection();
			InputStream in = urlc.getInputStream();
			Tag htmlDocument = Tools.createTagFromWebSource(in, false);
			List<Tag> rows = htmlDocument.getComplexTag("td");
			String fileName = new String();
			Tag rapidshareForms = null;
			for(Tag row : rows) {
				if("middle".equals(row.getAttribute("valign"))) {
					fileName = Tools.createWellFormattedFileName(row.getTagContent(true)) + ".pnf";
				}
				int index = row.toString().indexOf("form name=\"dlid01\"");
				if(index < 200 && index != -1) {
					rapidshareForms = row;
				}
			}
			
			OutputStream os = new FileOutputStream(destination + "/" + fileName);
			List<FormProperties> properties = rapidshareForms.getFormulars();
			for (FormProperties property : properties) {
				String action = "http://ddl-warez.org/" + property.getAction();
				property.setAction(action);
				Request request = new Request(property);
				in = request.post();
				htmlDocument = Tools.createTagFromWebSource(in, false);
				List<Tag> frames = htmlDocument.getComplexTag("frame");
				for (Tag frame : frames) {
					int index = frame.toString().indexOf("http://rapidshare");
					if (index < 100 && index != -1) {
						String source = frame.getAttribute("SRC");
						os.write((source + "\n").getBytes());
					}

				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		search("babylon", new File("/home/christopher/"));
	}
}
