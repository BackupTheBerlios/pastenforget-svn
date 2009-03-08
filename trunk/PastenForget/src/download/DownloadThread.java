package download;

public class DownloadThread {
	private Thread thread;
	private final Runnable target;

	public DownloadThread(Runnable target) {
		this.target = target;
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		try {
			System.out.println("Thread.stop");
			this.thread.stop();
		} catch(Exception e) { 
			System.out.println("DownloadThread: stop");
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			this.thread = new Thread(this.target);
			this.thread.start();
		} catch(Exception e) { 
			System.out.println("DownloadThread: start");
		}
	}
}
