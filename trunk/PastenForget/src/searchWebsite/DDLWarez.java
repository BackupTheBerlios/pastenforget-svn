package searchWebsite;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import parser.Tag;
import web.Connection;

public class DDLWarez extends SearchWebsite {
	private final String[] categories = new String[] { "apps", "movies",
			"games", "series", "mp3s", "xxx", "hoers", "cracks" };

	@Override
	public List<URL> filter(URL url) throws IOException {
		// TODO filter
		/*
		 * Connection webConnection = new Connection();
		 * webConnection.connect(url.toString()); Tag document =
		 * webConnection.doGet(); Tag description =
		 * document.getElementById("div", "description"); String text =
		 * description.toString().replaceAll("<br[^>]*>", "\n") .replaceAll("<[^>]+>",
		 * "");
		 * 
		 * String imageLink = new String(); for (Tag image :
		 * document.getSimpleTag("img")) { String source =
		 * image.getAttribute("src"); if (source != null &&
		 * (source.indexOf("captcha") != -1)) { imageLink =
		 * "http://ddl-warez.org/" + source; break; } }
		 * 
		 * webConnection.connect(imageLink); InputStream is =
		 * webConnection.getInputStream();
		 * 
		 * 
		 * OutputStream os = new
		 * FileOutputStream("/home/christopher/Desktop/captcha" +
		 * System.currentTimeMillis() + ".gif"); byte[] buffer = new byte[1024];
		 * int length = 0; while((length = is.read(buffer)) > 0) {
		 * os.write(buffer, 0, length); }
		 * 
		 * System.out.println("Enter Captcha Code"); String captchaCode = new
		 * BufferedReader(new InputStreamReader(System.in)).readLine(); Map<String,
		 * String> postParameters = new HashMap<String, String>();
		 * postParameters.put("AnimCaptcha", captchaCode);
		 * 
		 * 
		 * webConnection.connect(url.toString()); document =
		 * webConnection.doPost(postParameters);
		 */
		return new ArrayList<URL>();
	}

	@Override
	public void search(String keyWord) throws IOException {
		Connection webConnection = new Connection();
		for (String category : this.categories) {
			String searchLink = "http://ddl-warez.org/search.php?q="
					+ URLEncoder.encode(keyWord, "UTF-8") + "&cat=" + category;
			webConnection.connect(searchLink);
			Tag document = webConnection.getDocument();
			Tag mainContent = document.getElementById("div", "main_content");

			List<Tag> detailLinks = mainContent.getComplexTag("a");
			for (Tag detailLink : detailLinks) {
				String link = "http://ddl-warez.org/"
						+ detailLink.getAttribute("href");
				String[] details = detailLink.toString().replaceAll(
						"<[^>]+>|&nbsp;", "").split("[|]+");
				String name = details[0];
				String date = details[1];
				SearchEntry entry = new SearchEntry(this, link, name, date,
						"DDL-Warez.org");
				this.setChanged();
				this.notifyObservers(entry);
			}
		}
	}

	@Override
	public boolean stopSearch() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return "DDL-Warez";
	}
}
