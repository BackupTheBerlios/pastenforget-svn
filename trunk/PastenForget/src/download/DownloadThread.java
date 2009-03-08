package download;

public class DownloadThread {
	private Thread thread;
	private final Runnable target;
	private boolean stopped = true;
	private boolean started = false;

	public DownloadThread(Runnable target) {
		this.target = target;
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		if (!this.stopped) {
			this.thread.stop();
			this.started = false;
			this.stopped = true;
		}
	}

	public void start() {
		if(!this.started) {
			this.thread = new Thread(this.target);
			this.thread.start();
			this.started = true;
			this.stopped = false;
		}
	}
}
