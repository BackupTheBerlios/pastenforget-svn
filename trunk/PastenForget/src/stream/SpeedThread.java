package stream;

import download.Download;

public class SpeedThread implements Runnable  {

	private final long interval = 2000;
	
	private long lastSize = 0;
	
	private Download download = null;
	
	public SpeedThread (Download download){
		this.download = download;
	}

	@Override
	public void run() {
		long currentSize;
		double averageSpeed;
		while (!download.isCanceled() && !download.isStopped()) {
			try {
				this.wait(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentSize = download.getCurrentSize();
			
			averageSpeed = (double) (1/interval)*((currentSize - lastSize) / interval);
			
			lastSize = currentSize;
			download.setAverageSpeed(averageSpeed);
		}
	}
	
	
}
