package ui.gui;

import java.text.NumberFormat;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

import queue.Queue;
import download.Download;

public class DownloadTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -7804198019362646369L;

	private Queue queue;

	private final String[] columnIdentifiers = new String[] { "Dateiname",
			"Größe", "Status", "Fortschritt" };

	public DownloadTableModel(Queue queue) {
		this.queue = queue;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnIdentifiers[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return columnIdentifiers.length;
	}

	@Override
	public int getRowCount() {
		return this.queue.getDownloadList().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if ((rowIndex > -1) && (rowIndex + 1 <= getRowCount())
				&& (columnIndex < getColumnCount())) {
			Download download = this.queue.getDownloadList().get(rowIndex);
			switch (columnIndex) {
			case 0:
				return download.getFileName();
			case 1:
				String formatSize = new String(NumberFormat.getInstance().format(download.getFileSize()/(1024*1024)) + " MB");
				return formatSize;
			case 2:
				return download.getStatus();
			case 3:
				JProgressBar progressBar = new JProgressBar(0, 100);
				double currentSize = download.getCurrentSize();
				double fileSize = download.getFileSize();
				int prozent = 0;
				try {
					prozent = (int) ((currentSize / fileSize) * 100);
					progressBar.setValue(prozent);
				} catch (Exception e) {
					progressBar.setValue(0);
				}
				progressBar.setString(prozent + "%");
				progressBar.setStringPainted(true);
				return progressBar;
			default:
				return null;
			}
		} else {
			return null;
		}
	}

}
