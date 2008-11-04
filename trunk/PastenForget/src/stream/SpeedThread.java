package stream;

import download.Download;

public class SpeedThread implements Runnable  {

	private final long interval = 2;
	
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
				Thread.sleep(interval * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Speed called");
			currentSize = download.getCurrentSize();
			
			averageSpeed = (double) (1.0/interval)*((currentSize - lastSize) / interval);
			
			lastSize = currentSize;
			download.setAverageSpeed(averageSpeed);
		}
	}
	
	
}
