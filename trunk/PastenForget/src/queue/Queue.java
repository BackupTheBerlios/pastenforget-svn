package queue;

import java.util.Collections;
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
public class Queue extends Observable implements Observer {

	private List<Download> queue;

	public Queue() {
		this.queue = new LinkedList<Download>();
	}

	public List<Download> getDownloadList() {
		return queue;
	}

	public void addDownload(Download download) {
		queue.add(download);
		download.addObserver(this);
		update(null);
		startFirst();
	}

	private boolean startFirst() {
		if (!this.isEmpty() && (this.getDownload(0).isWait())) {
			getDownload(0).start();
			return true;
		}
		return false;
	}

	private Download getDownload(int index) {
		try {
			return queue.get(index);
		} catch (Exception e) {
			System.out.println("Queue: getDownload() failure");
			return null;
		}
	}

	public List<Download> getDownloads(int[] index) {
		List<Download> downloads = new LinkedList<Download>();
		for (int i : index) {
			downloads.add(this.getDownload(i));
		}
		return downloads;

	}

	public void downloadFinished(Download download) {
		queue.remove(download);
		startFirst();
		update(null);
	}

	private boolean removeDownload(int index) {
		try {
			this.getDownload(index).cancel();
			queue.remove(index);
		} catch (Exception e) {
			System.out.println("Queue: removeDownload() failure");
			return false;
		}
		return true;
	}

	public void removeDownloads(int[] index) {
		List<Download> downloads = this.getDownloads(index);
		int i;
		for (Download download : downloads) {
			i = queue.indexOf(download);
			this.removeDownload(i);
		}
		startFirst();
		update(null);
	}

	private boolean startDownload(int index) {
		if (!queue.isEmpty() && index < queue.size() && index > -1) {
			this.getDownload(index).start();
			return true;
		}
		return false;
	}

	public void startDownloads(int[] index) {
		List<Download> downloads = this.getDownloads(index);
		int i;
		for (Download download : downloads) {
			i = queue.indexOf(download);
			this.startDownload(i);
		}
		update(null);
	}
	
	private boolean stopDownload(int index) {
		try {
			this.getDownload(index).stop();
		} catch (Exception e) {
			System.out.println("Queue: stopDownload() failure");
			return false;
		}
		return true;
	}

	public void stopDownloads(int[] index) {
		try {
			List<Download> downloads = getDownloads(index);
			int i;
			for (Download download : downloads) {
				i = queue.indexOf(download);
				this.stopDownload(i);
			}
		} catch (Exception e) {
			System.out.println("Queue: stopDownloads() failure");
		}
		update(null);
	}
	
	private boolean setPriority(int index, int priority) {
		try {
			priority += this.getDownload(index).getPriority();
			this.getDownload(index).setPriority(priority);
		} catch (Exception e) {
			System.out.println("Queue: setPriority() failure");
			return false;
		}
		return true;
	}

	public void setPriorities(int[] index, int priority) {
		List<Download> downloads = this.getDownloads(index);
		int i;
		for (Download download : downloads) {
			i = queue.indexOf(download);
			this.setPriority(i, priority);
		}
		Collections.sort(queue, new DownloadComparator());
		update(null);
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public void putToEnd(Download download) {
		int i = queue.indexOf(download);
		try {
			stopDownload(i);
			queue.remove(i);
			queue.add(download);
			update(null);
			startFirst();
		} catch (Exception e) {
			System.out.println("Queue: putToEnd() failure");
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
