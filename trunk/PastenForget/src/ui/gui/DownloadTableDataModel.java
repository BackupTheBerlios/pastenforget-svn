package ui.gui;

import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

import queue.Queue;
import settings.Languages;
import download.Download;

public class DownloadTableDataModel extends AbstractTableModel {

	private static final long serialVersionUID = -7804198019362646369L;

	private Queue queue;
	private List<JProgressBar> progressBars = new LinkedList<JProgressBar>();

	private final String[] columnIdentifiers = new String[] {
			Languages.getTranslation("filename"),
			Languages.getTranslation("filesize"),
			Languages.getTranslation("status"),
			Languages.getTranslation("downloadspeed"),
			Languages.getTranslation("progress") };

	public DownloadTableDataModel(Queue queue) {
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
			if (this.queue.getDownloadList().size() != this.progressBars.size()) {
				this.createProgressBars();
			}
			switch (columnIndex) {
			case 0:
				return download.getFileName();
			case 1:
				Formatter formatSize = new Formatter().format("%.2f MB",
						((double) download.getExpectedSize() / (1024 * 1024)));
				return new String(formatSize.toString());
			case 2:
				return download.getStatus();
			case 3:
				// TODO Geschwindigkeit
				Formatter formatSpeed = new Formatter().format("%.2f KB/s",
						((double) 10000 / (1024)));
				return new String(formatSpeed.toString());
			case 4:
				JProgressBar progressBar = this.progressBars.get(rowIndex);
				double currentSize = download.getCurrentSize();
				double fileSize = download.getExpectedSize();
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

	private void createProgressBars() {
		this.progressBars = new LinkedList<JProgressBar>();
		int size = this.queue.getDownloadList().size();
		for (int i = 0; i<size; i++) {
			this.progressBars.add(new JProgressBar(0, 100));
		}
		
	}

}
