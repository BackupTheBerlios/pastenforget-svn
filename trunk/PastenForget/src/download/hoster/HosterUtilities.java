package download.hoster;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import download.Download;
import download.Status;

public class HosterUtilities {
	private final Download download;
	private InputStream iStream;
	private OutputStream oStream;
	private long startTime;

	public HosterUtilities(Download download) {
		this.download = download;
	}

	public void waitSeconds(int seconds) {
		for (int i = 0; i < seconds; i++) {
			try {
				Thread.sleep(1000);
				int timeleft = seconds - (i + 1);
				this.download.setStatus(Status.getWaitSec(timeleft));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void waitMinutes(int minutes) {
		for (int timeLeft = minutes; timeLeft >= 0; timeLeft--) {
			try {
				Thread.sleep(1000);
				this.download.setStatus(Status.getWaitMin(timeLeft));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void download(InputStream iStream) throws IOException {
		this.download.setStatus(Status.getActive());
		String filePath = this.download.getDestination().toString();
		String fileName = this.download.getFileName();
		this.iStream = iStream;
		this.oStream = new FileOutputStream(filePath + "/" + fileName);
		byte[] buffer = new byte[1024];
		int length = 0;
		this.startTime = System.currentTimeMillis();
		while ((length = this.iStream.read(buffer)) > 0) {
			this.download.setCurrentSize(this.download.getCurrentSize() + length);
			this.calculateAverageSpeed();
			this.oStream.write(buffer, 0, length);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(length);
		}
	}
	
	
	public void download(String directLink) throws IOException {
		this.download.setStatus(Status.getActive());
		URL directUrl = new URL(directLink);
		URLConnection connection = directUrl.openConnection();
		String filePath = this.download.getDestination().toString();
		String fileName = this.download.getFileName();
		this.iStream = connection.getInputStream();
		this.oStream = new FileOutputStream(filePath + "/" + fileName);
		byte[] buffer = new byte[1024];
		int length = 0;
		this.startTime = System.currentTimeMillis();
		while ((length = this.iStream.read(buffer)) > 0) {
			this.download.setCurrentSize(this.download.getCurrentSize() + length);
			this.calculateAverageSpeed();
			this.oStream.write(buffer, 0, length);
			System.out.println(length);
		}
	}

	private void calculateAverageSpeed() {
		long currentTime = System.currentTimeMillis();
		this.download.setAverageSpeed((double)this.download.getCurrentSize() / (double)(currentTime - this.startTime));
	}
	
	public void closeConnection() throws IOException {
		if (this.iStream != null) {
			this.iStream.close();
		}
		if (this.oStream != null) {
			this.oStream.close();
		}
	}

}
