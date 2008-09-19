package core;


/**
 * Thread, der zur Abarbeitung eines Downloadlinks dient
 * 
 * @author cschaedl
 */

public class DownloadThread extends Thread {

	private DownloadInterface download;
	private String url;

	public DownloadThread(DownloadInterface that) {
		this.download = that;
	}
	
	public void beenden() {
		System.exit(0);
	}
	
	
	public void run() {
			// TODO toString ueberdenken
			this.url = download.getUrl().toString();
			System.out.println("run()");
			download.downloadFileFromHoster(this.url);
	}
	
	public void finish() {

	}

}