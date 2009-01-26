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
		if ((x < 0) || (y < 0)) {
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

	/**
	 * Liest den InputStream einer Web-Quelle zeilenweise aus und puffert ihn in
	 * einem StringBuffer und erzeugt anschließend ein Wrapper-Objekt des Typs
	 * Tag
	 * 
	 * @param inputstream
	 * @return tag containing whole html/xml page
	 * @throws IOException
	 */
	public static Tag createTagFromWebSource(InputStream in, boolean report)
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

	/**
	 * Liest einen InputStream so lange aus, bis ein title-Tag gefunden wurde
	 * und ermittelt daraus den Content des title-Tags.
	 * 
	 * @param inputstream
	 * @return title tag
	 * @throws IOException
	 */
	public static String getTitleFromWebSource(InputStream in)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String currentLine;
		String title = new String();
		do {
			currentLine = reader.readLine();
			try {
				if (currentLine.indexOf("title") != -1) {
					String regex = "<title>[^<]+";
					Pattern p = Pattern
							.compile(regex, Pattern.CASE_INSENSITIVE);
					Matcher m = p.matcher(currentLine);
					while (m.find()) {
						title = m.group().replaceAll("<title>", "");
					}
				}
			} catch (Exception e) {
				/*
				 * currentLine ist null und somit ist die Funktion
				 * "x.indexOf(y)" nicht anwendbar. => InputStream vollständig
				 * ausgelesen!
				 */
				return new String();
			}
		} while (title.length() == 0);

		return new String(title);
	}

	/**
	 * Entfernt alle unzulässigen Teilstrings aus einem Dateinamen
	 * 
	 * @param filename
	 * @return well formatted filename
	 */
	public static String createWellFormattedFileName(String oldFileName) {
		return new String(oldFileName.replaceAll("&[^;]+;", "").replaceAll(
				"/+", "-").replaceAll("\\+", "-").replace('?', '_')).replace(
				".", "_").replaceAll("\\s+", "_");
	}

	public static void main(String[] args) {
		System.out
				.println(createWellFormattedFileName("Frida  DVDrip-DivX/Avi"));
	}
}
