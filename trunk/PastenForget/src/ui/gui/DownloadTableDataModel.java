package ui.gui;

import java.util.Formatter;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

import queue.Queue;
import settings.Languages;
import download.Download;

public class DownloadTableDataModel extends AbstractTableModel {

	private static final long serialVersionUID = -7804198019362646369L;

	private Queue queue;

	private short progress = 0;

	private final String[] columnIdentifiers = new String[] {
			Languages.getTranslation("filename"),
			Languages.getTranslation("filesize"),
			Languages.getTranslation("status"),
			Languages.getTranslation("progress") };

	public DownloadTableDataModel(Queue queue) {
		this.queue = queue;
	}

	private String getProgress() {
		switch (progress) {
		case 0:
			progress++;
			return "|";
		case 1:
			progress++;
			return "/";
		case 2:
			progress++;
			return "-";
		case 3:
			progress++;
			return "\\";

		case 4:
			progress++;
			return "|";
		case 5:
			progress++;
			return "/";
		case 6:
			progress++;
			return "-";

		case 7:
			progress = 0;
			return "\\";
		default:
			return "|";
		}
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
				Formatter formatSize = new Formatter().format("%.2f MB",
						((double) download.getFileSize() / (1024 * 1024)));
				return new String(formatSize.toString());
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
				progressBar.setString(prozent + "%  " + getProgress());
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
