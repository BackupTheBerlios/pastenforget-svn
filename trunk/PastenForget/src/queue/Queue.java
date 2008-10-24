package queue;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import middleware.ObserverMessageObject;

import download.Download;

/**
 * Warteschlange fuer Downloads.
 * 
 * @author executor
 * 
 */
public class Queue extends Observable implements QueueInterface, Observer {

	private List<Download> queue;

	private final Download downloadDefault;

	public Queue() {
		this.queue = new LinkedList<Download>();
		downloadDefault = new Download();
		downloadDefault.setFileName("Kein Download gestartet!");
		downloadDefault.setStatus("0,0%");
	}

	@Override
	public List<Download> getDownloadList() {
		return queue;
	}

	@Override
	public void addDownload(Download download) {
		queue.add(download);
		download.addObserver(this);
		update(null);
		startFirst();
	}

	@Override
	public Download getDownload(int index) {
		try {
			return queue.get(index);
		} catch (Exception e) {
			System.out.println("Queue: getDownload() failure");
			return null;
		}
	}

	@Override
	public void downloadFinished(Download download) {
		try {
			queue.remove(download);
			startFirst();
			update(null);
		} catch (Exception e) {
			System.out.println("Queue: downloadFinished() failure");
		}
	}

	@Override
	public void removeDownload(int index) {
		try {
			queue.get(index).cancel();
			queue.remove(index);
		} catch (Exception e) {
			System.out.println("Queue: removeDownload() failure");
		}
		startFirst();
		update(null);
	}

	@Override
	public void removeDownloads(int[] index) {
		try {
			List<Download> downloads = getDownloads(index);
			int i;
			for (Download download : downloads) {
				i = queue.indexOf(download);
				removeDownload(i);
			}
		} catch (Exception e) {
			System.out.println("Queue: removeDownloads() failure");
		}
	}

	@Override
	public void startDownload(int index) {
		if (!queue.isEmpty() && index < queue.size() && index > -1) {
			queue.get(index).start();
		}
		update(null);
	}

	@Override
	public void stopDownload(int index) {
		try {
			queue.get(index).stop();
		} catch (Exception e) {
			System.out.println("Queue: stopDownload() failure");
		}
		update(null);
	}

	@Override
	public void stopDownloads(int[] index) {
		try {
			List<Download> downloads = getDownloads(index);
			int i;
			for (Download download : downloads) {
				i = queue.indexOf(download);
				stopDownload(i);
			}
		} catch (Exception e) {
			System.out.println("Queue: stopDownloads() failure");
		}
	}

	@Override
	public void startFirst() {
		if (!isEmpty() && !(getDownload(0).isStarted())
				&& !(getDownload(0).isStopped())) {
			getDownload(0).start();
		}
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public void putToEnd(Download download) {
		try {
			stopDownload(download.getIndex());
			queue.remove(download.getIndex());
			queue.add(download);
			update(null);
			startFirst();
		} catch (Exception e) {
			System.out.println("Queue: putToEnd() failure");
		}
	}

	private List<Download> getDownloads(int[] index) {
		try {
			List<Download> downloads = new LinkedList<Download>();
			for (int i : index) {
				downloads.add(queue.get(i));
			}
			return downloads;
		} catch (Exception e) {
			System.out.println("Queue: getDownloads() failure");
			return null;
		}
	}

	/**
	 * Benachrichtige Observer.
	 */
	private void update(ObserverMessageObject omo) {
		setChanged();
		if (omo != null) {
			notifyObservers(omo);
		} else {
			omo = new ObserverMessageObject(this);
			notifyObservers(omo);
		}
	}

	@Override
	public void update(Observable sender, Object message) {
		if (message.getClass() == ObserverMessageObject.class) {
			ObserverMessageObject omo = (ObserverMessageObject) message;
			update(omo);
		}
	}

}
