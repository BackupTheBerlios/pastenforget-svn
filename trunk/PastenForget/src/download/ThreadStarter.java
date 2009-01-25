package download;

import java.io.File;
import java.net.URL;

import download.hoster.Rapidshare;



public class ThreadStarter {
	public static void main(String[] args) throws Exception {
		Download rs = new Rapidshare();
		rs.setUrl(new URL("http://rapidshare.com/files/134109804/Metallica_-_Some_Kind_of_Monster.part1.rar"));
		rs.setDestination(new File("/home/christopher/Desktop"));
		rs.setFileName("nero.exe");
		rs.start();
	}
}
