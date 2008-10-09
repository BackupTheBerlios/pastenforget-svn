package ui.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class DownloadTableRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value == null) {
			return new JLabel();
		}
		if (value.getClass() == JProgressBar.class) {
			return (JProgressBar) value;
		}
		return (JLabel) value;
	}

}
