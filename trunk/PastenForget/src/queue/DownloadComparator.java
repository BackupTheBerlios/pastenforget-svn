package queue;

import java.util.Comparator;

import download.Download;

public class DownloadComparator implements Comparator<Download> {

	@Override
	public int compare(Download arg0, Download arg1) {
		if (arg0.getPriority() < arg1.getPriority())
			return -1;
		if (arg0.getPriority() > arg1.getPriority())
			return 1;
		return 0;
	}

}

/*
 * setPrio() {
 * 	download.setPrio()
 * 	Collections.sort(queue, new DownloadComparator());
 */
	