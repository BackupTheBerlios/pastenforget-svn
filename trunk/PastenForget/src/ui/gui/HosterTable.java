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
import javax.swing.JScrollPane;
import javax.swing.JTable;

import middleware.Middleware;
import queue.Queue;
import download.Download;
import download.hoster.HosterEnum;

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

	private JPopupMenu dropDownMenu;

	private JMenuItem cancel;

	public HosterTable(Middleware middleware, HosterEnum hoster) {
		this.middleware = middleware;
		this.name = hoster.getName();

		queue = middleware.getQueue(hoster.getKey());
		queue.addObserver(this);

		model = new DownloadTableModel(queue);
		table = new JTable(model);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setDefaultRenderer(javax.swing.JProgressBar.class,
				new DownloadTableRenderer());

		this.dropDownMenu = new JPopupMenu();
		cancel = new JMenuItem("Abbrechen");
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		this.dropDownMenu.add(cancel);

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
		if ("cancel".equals(source)) {
			dropDownMenu.setVisible(false);
			queue.removeDownload(table.getSelectedRow());
		}
	}
	
	public void update(Observable sender, Object message) {
		if ("queue".equals(message)) {
			model.newData();
		} else {
			Download download = (Download) message;
			model.updateData(download.getIndex());
		}
	}

	class Listener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == 3) {
				if (dropDownMenu.isVisible()) {
					dropDownMenu.setVisible(false);
				} else {
					dropDownMenu.setLocation(e.getLocationOnScreen());
					dropDownMenu.setVisible(true);
					dropDownMenu.setEnabled(true);
				}
			} else {
				dropDownMenu.setVisible(false);
			}
		}
	}

}
