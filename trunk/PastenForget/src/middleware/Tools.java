package middleware;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import download.hoster.HosterEnum;

public class Tools {

	public static String getProgramPath() {
		String path = System.getProperty("java.class.path");
		String regex = "[^/]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(path);
		int pos = 0;
		while (m.find()) {
			pos = m.start();
		}
		return path.substring(0, pos);
	}
	
	public static int checkHoster(String url) {
		for (HosterEnum hoster : HosterEnum.values()) {
			if (url.indexOf(hoster.getUrl()) != -1) {
				return hoster.getKey();
			}

		}
		return HosterEnum.OTHER.getKey();
	}
}
