package filtration;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import parser.Tag;
import middleware.Tools;;

public class Packetnews {


	public static void search(String keyword) throws Exception {
		Integer page = 1;
		List<RequestPackage> packages = new ArrayList<RequestPackage>();
		List<List<String>> entries = new ArrayList<List<String>>();
		Integer entryCounter = 0;
		do {
			String link = "http://packetnews.com/search.php?page=" + page
					+ "&type=0&kw=" + URLEncoder.encode(keyword, "UTF-8");
			URL url = new URL(link);
			InputStream in = url.openStream();
			Tag htmlDocument = Tools.createTagFromWebSource(in, false);

			List<Tag> trs = htmlDocument.getComplexTag("tr");

			entryCounter = 0;
			for (Tag tr : trs) {
				if (tr.toString().indexOf("<tr bgcolor=#999999>") == 0) {
					entryCounter++;
					List<Tag> tds = tr.getComplexTag("td");
					List<String> entry = new ArrayList<String>();
					for (Tag td : tds) {
						String parsedTag = td.getTagContent(false).replaceAll(
								"<[^a]{1}[^>]*>", "").replaceAll("&[^;]+;", "");
						entry.add(parsedTag);
					}
					entries.add(entry);
				}
			}
			page++;
		} while (entryCounter > 0);

		String botName = new String();
		String active = new String();
		String slots = new String();
		String queue = new String();
		String speed = new String();
		String ircServer = new String();
		String ircChannel = new String();
		String downloaded = new String();
		String fileSize = new String();
		String fileName = new String();
		for (List<String> entry : entries) {
			if (!"".equals(entry.get(0))) {
				botName = entry.get(0);
				active = entry.get(1);
				slots = entry.get(2);
				queue = entry.get(3);
				speed = entry.get(4);
			}
			Tag link = new Tag(entry.get(5)).getComplexTag("a").get(0);
			String[] splits = link.getAttribute("onClick").replace(
					"toclip('/ctcp ", "").replace("');", "").split("\\s+");
			String packageNumber = splits[3];
			String[] irc = link.getAttribute("href").replace("irc://", "")
					.split("/");
			ircServer = irc[0];
			ircChannel = irc[1];
			downloaded = entry.get(6);
			fileSize = entry.get(7);
			fileName = entry.get(8);
			
			slots = (slots.equals("")) ? "n/a" : slots;
			
			System.out.println("Server: " + ircServer);
			System.out.println("Channel: " + ircChannel);
			System.out.println("Bot: " + botName);
			System.out.println("Package: " + packageNumber);
			System.out.println("Free Slots: " + slots);	
			System.out.println("================================");
			
			packages.add(new RequestPackage(active, slots, queue, speed, downloaded,
					ircServer, ircChannel, botName, packageNumber, fileSize,
					fileName));
		}
		System.out.println("==> " + packages.size()  + " Treffer");
	}
	
	public static void main(String[] args) throws Exception {
		search("the chaser");
	}
	
	
}
