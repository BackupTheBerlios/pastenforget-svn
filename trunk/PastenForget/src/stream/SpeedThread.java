package stream;

import java.util.ArrayList;
import java.util.List;

import download.Download;

public class SpeedThread implements Runnable {

	private final short interval = 10;

	//private long lastSize = 0;

	private Download download = null;
	
	private List<Long> history; 

	public SpeedThread(Download download) {
		this.download = download;
		this.history = new ArrayList<Long>();
	}

	@Override
	public void run() {
		long currentSize;
		double averageSpeed;
		while (!download.isCanceled() && !download.isStopped()) {
			currentSize = download.getCurrentSize();
			history.add(currentSize);
			averageSpeed = (double) ((currentSize - history.get(0)) / ((history.size() < this.interval) ? history.size() : interval));
			if (history.size() > this.interval) {
				history.remove(0);
			}
			
			
			/*currentSize = download.getCurrentSize();
			averageSpeed = (double) ((currentSize - lastSize) / interval);
			lastSize = currentSize;*/
			download.setAverageSpeed(averageSpeed);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out
						.println("SpreadThread: failure " + download.getUrl());
			}
		}
	}

}
