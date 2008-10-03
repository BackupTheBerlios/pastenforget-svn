package queue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import download.Download;

/**
 * Warteschlange fuer Downloads.
 * 
 * @author cpieloth
 * 
 */
public class Queue extends Observable implements QueueInterface {

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
		update();
	}

	/**
	 * Gibt true zurueck, wenn Warteschlange leer ist.
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	// TODO JavaDoc
	public String[][] getDownloads() {
		String [][] downloads = new String [queue.size()][4];
		Iterator<Download> i = queue.iterator();
		Download download = downloadDefault;
		for (int c = 0; i.hasNext(); c++) {
			download = i.next();
			downloads[c][0] = download.getFileName();
			downloads[c][1] = new Long(download.getFileSize()).toString();
			downloads[c][2] = download.getStatus();
			downloads[c][3] = new Long(download.getCurrentSize()).toString();			
		}
		return downloads;
	}
	
	/**
	 * Benachrichtige Observer.
	 */
	private void update() {
		setChanged();
		notifyObservers("queue");
	}
}
