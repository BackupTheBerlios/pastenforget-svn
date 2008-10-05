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
		if (!queue.isEmpty() && index < queue.size()) {
			queue.get(index).cancel();
			queue.remove(index);
		}
		startFirst();
		update();
	}

	@Override
	public void startDownload(int index) {
		if (!queue.isEmpty() && index < queue.size()) {
			queue.get(index).start();
		}
		update();
	}

	@Override
	public void stopDownload(int index) {
		if (!queue.isEmpty() && index < queue.size()) {
			queue.get(index).stop();
		}
		update();
	}

	@Override
	public void startFirst() {
		if (!isEmpty() && !(getCurrent().isStarted())) {
			getCurrent().start();
		}
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * Benachrichtige Observer.
	 */
	private void update() {
		setChanged();
		notifyObservers("queue");
	}

	public void update(Observable sender, Object message) {
		if ("download".equals(message)) {
				setChanged();
				notifyObservers((Download) sender);
		}
	}

}
