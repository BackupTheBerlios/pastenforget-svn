package filtration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import middleware.Tools;
import parser.Tag;
import exception.CancelException;
import exception.TagNotSupportedException;

public class Packetnews extends Observable implements Runnable {
	private final String keyword;
	private boolean canceled;

	public Packetnews(String keyword) {
		this.keyword = keyword;
	}

	public synchronized void cancel() {
		this.canceled = true;
	}

	private synchronized boolean isCanceled() {
		return this.canceled;
	}

	@Override
	public void run() {
		try {
			Integer page = 1;
			Integer entryCounter = 0;
			do {
				String link = "http://packetnews.com/search.php?page=" + page
						+ "&type=0&kw="
						+ URLEncoder.encode(this.keyword, "UTF-8");
				URL url = new URL(link);
				InputStream in = url.openStream();
				Tag htmlDocument = Tools.createTagFromWebSource(in, false);

				List<Tag> trs = htmlDocument.getComplexTag("tr");

				entryCounter = 0;

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

				for (Tag tr : trs) {
					if (tr.toString().indexOf("<tr bgcolor=#999999>") == 0) {
						entryCounter++;
						List<Tag> tds = tr.getComplexTag("td");
						List<String> entry = new ArrayList<String>();
						for (Tag td : tds) {
							String parsedTag = td.getTagContent(false)
									.replaceAll("<[^a]{1}[^>]*>", "")
									.replaceAll("&[^;]+;", "");
							entry.add(parsedTag);
						}
						if (this.isCanceled()) {
							throw new CancelException();
						}

						if (!"".equals(entry.get(0))) {
							botName = entry.get(0);
							active = entry.get(1);
							slots = entry.get(2);
							queue = entry.get(3);
							speed = entry.get(4);
						}
						Tag a = new Tag(entry.get(5)).getComplexTag("a").get(0);
						String[] splits = a.getAttribute("onClick").replace(
								"toclip('/ctcp ", "").replace("');", "").split(
								"\\s+");
						String packageNumber = splits[3];
						String[] irc = a.getAttribute("href").replace("irc://",
								"").split("/");
						ircServer = irc[0];
						ircChannel = irc[1];
						downloaded = entry.get(6);
						fileSize = entry.get(7);
						fileName = entry.get(8);

						System.out.println("Server: " + ircServer);
						System.out.println("Channel: " + ircChannel);
						System.out.println("Bot: " + botName);
						System.out.println("Package: " + packageNumber);

						this.setChanged();
						this.notifyObservers(new RequestPackage(active, slots,
								queue, speed, downloaded, ircServer,
								ircChannel, botName, packageNumber, fileSize,
								fileName));

					}
				}
				page++;
			} while (entryCounter > 0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TagNotSupportedException te) {
			te.printStackTrace();
		} catch (CancelException ce) {
			System.out.println("Suche nach \"" + this.keyword
					+ "\" abgebrochen!");
		}
	}

	public static void main(String[] args) throws Exception {
		Packetnews news = new Packetnews("the chaser");
		new Thread(news).start();
	}

}
