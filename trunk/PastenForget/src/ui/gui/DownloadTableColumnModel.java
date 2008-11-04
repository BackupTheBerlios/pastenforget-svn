package ui.gui;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import settings.Languages;

public class DownloadTableColumnModel extends DefaultTableColumnModel {

	private static final long serialVersionUID = -7804198019362646369L;

	private final String[] columnIdentifiers = new String[] {
			Languages.getTranslation("filename"),
			Languages.getTranslation("filesize"),
			Languages.getTranslation("status"),
			"Datenrate",
			Languages.getTranslation("progress") };

	public DownloadTableColumnModel() {
		TableColumn column = null;
		for (int i = 0; i < columnIdentifiers.length; i++) {
			column = new TableColumn(i);
			column.setHeaderValue(columnIdentifiers[i]);
			this.addColumn(column);
		}
		this.getColumn(4).setCellRenderer(new DownloadTableRenderer());
	}

}