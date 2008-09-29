package download.hoster;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import queue.Queue;

import download.Download;
import download.DownloadInterface;
import download.ServerConnection;
import download.ServerDownload;

public class Megaupload extends Download implements DownloadInterface {

	public Megaupload(URL url, Queue queue) {
		this.setUrl(url);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName("megaupload");
	}

}
