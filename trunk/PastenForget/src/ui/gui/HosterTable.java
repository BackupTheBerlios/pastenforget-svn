package ui.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import middleware.Middleware;
import core.Queue;

/**
 * Gemeinsame GUI-Attribute aller Hoster.
 * 
 * http://www.java-tips.org/java-se-tips/javax.swing/read-a-data-file-into-a-jtable-and-reload-if-data-file-have-ch.html
 * 
 * @author cpieloth
 * 
 */
public class HosterTable extends JScrollPane implements Observer,
		ActionListener {

	private static final long serialVersionUID = -7775517952036303028L;

	protected Middleware middleware;

	protected Queue queue;

	protected DefaultTableModel model;

	protected JTable table;
	
	private JPopupMenu menu;

	public HosterTable() {

		String[] columnNames = new String[] { "Dateiname", "Größe", "Status",
				"Fortschritt" };

		String[][] data = new String[][] { { "Kein Download gestartet", "-",
				"-", "-" } };

		model = new DefaultTableModel(data, columnNames);

		table = new JTable(model);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		
		this.menu = new JPopupMenu();
		this.menu.add(new JMenuItem("Abbrechen"));
		
		

		table.addMouseListener(new Listener());

		this.setViewportView(table);

	}

	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("Abbrechen".equals(source)) {
			middleware.cancel(queue);
		}
	}

	public void update(Observable arg0, Object arg1) {
		if (arg1.equals("queue")) {
			for (int i = 0; i < model.getRowCount(); i++) {
				model.removeRow(i);
			}
			String[][] downloads = queue.getDownloads();
			for (int i = 0; i < downloads.length; i++) {
				model.addRow(downloads[i]);
			}
			queue.getCurrent().addObserver(this);
		}
		if (arg1.equals("downloadFileName")) {
			String[][] downloads = queue.getDownloads();
			model.setValueAt(downloads[0][0], 0, 0);
		}
		if (arg1.equals("downloadFileSize")) {
			String[][] downloads = queue.getDownloads();
			model.setValueAt(downloads[0][1], 0, 1);
		}
		if (arg1.equals("downloadStatus")) {
			String[][] downloads = queue.getDownloads();
			model.setValueAt(downloads[0][2], 0, 2);
		}
		if (arg1.equals("downloadCurrentSize")) {
			String[][] downloads = queue.getDownloads();
			model.setValueAt(downloads[0][3], 0, 3);
			;
		}
	}

	class Listener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (menu.isVisible()) {
				menu.setVisible(false);
			} else {
				menu.setVisible(true);
			}

			JTable table = (JTable) e.getComponent();
			// queue.removeCurrent();
		}
	}

}
