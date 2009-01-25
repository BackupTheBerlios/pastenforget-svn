package download;

public class DownloadThread {
	private Thread thread;
	private final Runnable target;
	
	public DownloadThread(Runnable target) {
		this.target = target;
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		this.thread.stop();
	}
	
	public void start() {
		this.thread = new Thread(this.target);
		this.thread.start();
	}	
}
