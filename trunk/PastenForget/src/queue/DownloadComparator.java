package queue;

import java.util.Comparator;

import download.Download;

public class DownloadComparator implements Comparator<Download> {

	@Override
	public int compare(Download download1, Download download2) {
		if (download1.getPriority() < download2.getPriority())
			return 1;
		if (download1.getPriority() > download2.getPriority())
			return -1;
		return 0;
	}

}	