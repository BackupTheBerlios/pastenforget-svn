package download.hoster;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import download.Download;
import download.DownloadThread;
import download.Status;

import parser.Formular;
import parser.Tag;
import web.Connection;


public class Netload extends Download {
	public Netload(URL url, File destination) {
		super(url, destination);
	}

	private int counter = 0;

	@Override
	public boolean cancel() {
		if (this.getThread() != null) {
			this.getThread().stop();
		}
		this.setStatus(Status.getCanceled());
		return true;
	}

	@Override
	public boolean start() {
		this.setStart(true);
		this.setThread(new DownloadThread((Runnable)this));
		this.getThread().start();
		this.setStatus(Status.getStarted());
		return false;
	}

	@Override
	public boolean stop() {
		if (this.getThread() != null) {
			this.getThread().stop();
		}
		this.setStop(true);
		this.setStatus(Status.getStopped());
		return true;
	}

	public boolean restart() {
		this.getThread().stop();
		this.getThread().start();
		this.setStart(true);
		return true;
	}

	@Override
	public void prepareDownload() {
		HosterUtilities util = new HosterUtilities(this);
		try {
			Connection webConnection = new Connection();
			webConnection.connect(this.getUrl());
			Tag document = webConnection.getDocument(true);
			
			
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
}