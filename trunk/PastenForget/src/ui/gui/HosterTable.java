package ui.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import middleware.Middleware;
import queue.Queue;
import download.hoster.Hoster;

/**
 * Gemeinsame GUI-Attribute aller Hoster.
 * 
 * http://www.java-tips.org/java-se-tips/javax.swing/read-a-data-file-into-a-
 * jtable-and-reload-if-data-file-have-ch.html
 * 
 * @author cpieloth
 * 
 */
public class HosterTable extends JScrollPane implements Observer,
		ActionListener {

	private static final long serialVersionUID = -7775517952036303028L;

	protected Middleware middleware;

	protected Queue queue;

	protected DownloadTableModel model;

	protected String name;

	protected JTable table;

	private JPopupMenu menu;

	public HosterTable(Middleware middleware, Hoster hoster) {
		this.middleware = middleware;
		this.name = hoster.getName();

		queue = middleware.getQueue(hoster.getKey());
		queue.addObserver(this);

		model = new DownloadTableModel(queue);
		table = new JTable(model);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setDefaultRenderer(javax.swing.JProgressBar.class,new DownloadTableRenderer());

		this.menu = new JPopupMenu();
		this.menu.add(new JMenuItem("Abbrechen"));

		table.addMouseListener(new Listener());

		this.setViewportView(table);
	}

	public String getHoster() {
		return name;
	}

	public Component getComponent() {
		return this;
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
			model.newData();
		} else {
			Integer i = (Integer)arg1;
			model.updateData(i.intValue());
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
			queue.removeDownload(table.getSelectedRow());
		}
	}

}
