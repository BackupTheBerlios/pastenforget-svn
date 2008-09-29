package core;

import download.Download;

/**
 * Diese Schnittstellen muessen von jeder Warteschlange bereitgestellt werden.
 * 
 * @author cpieloth
 * 
 */
public interface QueueInterface {

	public Download getCurrent();

	public Download getNext();

	public void addDownload(Download download);

	public void removeCurrent();
	
	public boolean isEmpty();
}
