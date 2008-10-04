package queue;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import download.Download;

/**
 * Warteschlange fuer Downloads.
 * 
 * @author cpieloth
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
		download.setIndex(queue.size()-1);
		update();
		startFirst();
	}

	@Override
	public Download getCurrent() {
		if (!queue.isEmpty()) {
			return queue.get(0);
		} else {
			return downloadDefault;
		}
	}

	@Override
	public void removeCurrent() {
		removeDownload(0);
	}
	
	@Override
	public void removeDownload(int index) {
		if (!queue.isEmpty()) {
			queue.get(index).stop();
			queue.remove(index);
			startFirst();
		}
		updateDownloadIndex();
		update();
	}
	
	@Override
	public void startFirst() {
		if (queue.size() > 0 && !getCurrent().hasStarted()) {
			queue.get(0).start();
		}
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	private void updateDownloadIndex() {
		for (Download download : queue) {
			download.setIndex(queue.indexOf(download));
		}
	}

	/**
	 * Benachrichtige Observer.
	 */
	private void update() {
		setChanged();
		notifyObservers("queue");
	}

	public void update(Observable arg0, Object arg1) {
		if ("download".equals(arg1)) {
			setChanged();
			notifyObservers((Download) arg0);
		}
	}
}
