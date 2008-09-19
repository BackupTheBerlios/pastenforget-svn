package core;

public class StopThread {
	private boolean stopped;
	
	public StopThread() {
		this.stopped = false;
	}
	
	public synchronized void stopThread() {
		this.stopped = true;
	}
	
	public synchronized boolean isStopped() {
		return this.stopped;
	}

}
