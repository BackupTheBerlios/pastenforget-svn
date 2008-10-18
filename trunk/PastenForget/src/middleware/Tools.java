package middleware;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.Tag;
import download.hoster.HosterEnum;

/**
 * Sammelklasse fuer allgemeine Methoden.
 * 
 * @author executor
 *
 */

public class Tools {
	
	public static Point getCenteredLocation(Dimension windowSize) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - windowSize.width) / 2;
		int y = (screenSize.height - windowSize.height) / 2;
		if ((x < 0) || (y <0)) {
			x = 10;
			y = 10;
		}
		return new Point(x, y);
	}

	public static File getProgramPath() {
		String path = System.getProperty("java.class.path");
		String regex = "[^/]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(path);
		int pos = 0;
		while (m.find()) {
			pos = m.start();
		}
		return new File(path.substring(0, pos));
	}

	public static int checkHoster(String url) {
		for (HosterEnum hoster : HosterEnum.values()) {
			if (url.indexOf(hoster.getUrl()) != -1) {
				return hoster.getKey();
			}

		}
		return HosterEnum.OTHER.getKey();
	}

	public static Tag getTagFromInputStream(InputStream in, boolean report)
			throws IOException {
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = new String();

		if (report) {
			while ((line = reader.readLine()) != null) {
				System.out.println(": " + line);
				buffer.append(line);
			}
		} else {
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		}

		return new Tag(buffer.toString());
	}

	public static Tag getTitleFromInputStream(InputStream in)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String currentLine;
		String title = new String();
		do {
			currentLine = reader.readLine();
			if (currentLine == null) {
				break;
			}
			if (currentLine.indexOf("title") != -1) {
				String regex = "<title>[^<]+";
				Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(currentLine);
				while (m.find()) {
					title = m.group().replaceAll("<title>", "");
				}
			}

		} while (title.length() == 0);

		if (title.length() == 0) {
			return new Tag("<title></title>");
		} else {
			return new Tag(title);
		}
	}
}
