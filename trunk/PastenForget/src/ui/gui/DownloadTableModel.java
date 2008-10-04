package ui.gui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import queue.Queue;
import download.Download;

public class DownloadTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -7804198019362646369L;

	private Vector<Vector<Object>> data;

	private Queue queue;

	private final String[] columnIdentifiers = new String[] { "Dateiname",
			"Größe", "Status", "Fortschritt" };

	private final Vector<String> columnIdentifiersVector = new Vector<String>(
			java.util.Arrays.asList(columnIdentifiers));

	public DownloadTableModel(Queue queue) {
		this.queue = queue;
		this.data = transformData(queue);
		this.setDataVector(this.data, columnIdentifiersVector);
	}

	@Override
	public int getColumnCount() {
		return columnIdentifiersVector.size();
	}

	public Vector<Vector<Object>> transformData(Queue queue) {
		Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
		Vector<Object> dataRow;
		// JProgressBar progressBar;
		for (Download download : queue.getDownloadList()) {
			dataRow = new Vector<Object>();
			dataRow.add(download.getFileName());
			dataRow.add(download.getFileSize());
			dataRow.add(download.getStatus());
			/*
			 * progressBar = new JProgressBar(0, 100); progressBar.setValue(0);
			 * progressBar.setStringPainted(true); progressBar.setEnabled(true);
			 * progressBar.setSize(100, 10);
			 */
			dataRow.add(download.getCurrentSize());
			rows.add(dataRow);
		}
		return rows;
	}

	public void newData() {
		this.data = transformData(this.queue);
		this.setDataVector(this.data, columnIdentifiersVector);
		this.fireTableDataChanged();
	}

	public void updateData(int row) {
		if (row + 1 <= this.queue.getDownloadList().size()) {
			Download download = this.queue.getDownloadList().get(row);
			Vector<Object> dataRow = new Vector<Object>();
			dataRow.add(download.getFileName());
			dataRow.add(download.getFileSize());
			dataRow.add(download.getStatus());
			/*
			 * JProgressBar progressBar = new JProgressBar(0, 100);
			 * progressBar.setValue(0); progressBar.setStringPainted(true);
			 */
			dataRow.add(download.getCurrentSize());
			this.data.set(row, dataRow);
			this.fireTableRowsUpdated(row, row);
		}
	}

}
