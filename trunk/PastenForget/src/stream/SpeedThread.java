package stream;

import download.Download;

public class SpeedThread implements Runnable {

	private final short interval = 3;

	private long lastSize = 0;

	private Download download = null;

	public SpeedThread(Download download) {
		this.download = download;
	}

	@Override
	public void run() {
		long currentSize;
		double averageSpeed;
		while (!download.isCanceled() && !download.isStopped()) {
			currentSize = download.getCurrentSize();
			averageSpeed = (double) ((currentSize - lastSize) / interval);
			lastSize = currentSize;
			download.setAverageSpeed(averageSpeed);
			try {
				Thread.sleep(interval * 1000);
			} catch (InterruptedException e) {
				System.out
						.println("SpreadThread: failure " + download.getUrl());
			}
		}
	}

}
