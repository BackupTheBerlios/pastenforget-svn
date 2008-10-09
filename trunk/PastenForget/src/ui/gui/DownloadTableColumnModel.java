package ui.gui;

import java.text.NumberFormat;

import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableColumnModel;

import queue.Queue;
import download.Download;

public class DownloadTableColumnModel extends DefaultTableColumnModel {

	private static final long serialVersionUID = -7804198019362646369L;

	private Queue queue;

	private final String[] columnIdentifiers = new String[] { "Dateiname",
			"Größe", "Status", "Fortschritt" };

	public DownloadTableColumnModel(Queue queue) {
		this.queue = queue;
		this.getColumn(3).getCellRenderer().setCellRenderer(
				new DownloadTableRenderer());
	}
	
	
}