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

	public List<Download> getQueue() {
		return queue;
	}

	private final Download downloadDefault ;

	public Queue() {
		this.queue = new LinkedList<Download>();
		downloadDefault = new Download();
		downloadDefault.setFileName("Kein Download gestartet!");
		downloadDefault.setStatus("0,0%");
	}

	/**
	 * Fuegt einen Download an die Warteschlange an.
	 */
	public void addDownload(Download download) {
		queue.add(download);
		download.addObserver(this);
		download.setIndex(queue.size()-1);
		update();
		if (queue.size() == 1) {
			queue.get(0).start();
		}
	}

	/**
	 * Gibt den derzeiten Download zurueck.
	 * 
	 * @return
	 */
	public Download getCurrent() {
		if (!queue.isEmpty()) {
			return queue.get(0);
		} else {
			return downloadDefault;
		}
	}

	/**
	 * Gibt den naechsten Download zurueck.
	 */
	public Download getNext() {
		if (queue.size() > 1) {
			return queue.get(1);
		} else {
			return downloadDefault;
		}
	}

	/**
	 * Entfernt den derzeitigen Download.
	 */
	public void removeCurrent() {
		if (!queue.isEmpty()) {
			queue.get(0).stop();
			queue.remove(0);
			if (queue.size() > 0) {
				queue.get(0).start();
			}
		}
		updateDownloadIndex();
		update();
	}
	
	public void removeDownload(int selectedRow) {
		if (!queue.isEmpty()) {
			queue.get(selectedRow).stop();
			queue.remove(selectedRow);
			if (queue.size() > 0) {
				queue.get(0).start();
			}
		}
		updateDownloadIndex();
		update();
	}

	private void updateDownloadIndex() {
		for (Download download : queue) {
			download.setIndex(queue.indexOf(download));
		}
	}

	/**
	 * Gibt true zurueck, wenn Warteschlange leer ist.
	 */
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

	@Override
	public void update(Observable arg0, Object arg1) {
		Download download = (Download) arg0;
		setChanged();
		notifyObservers(download.getIndex());
	}
}
