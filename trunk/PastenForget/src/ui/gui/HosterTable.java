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
public class HosterTable extends JScrollPane implements Observer {

	private static final long serialVersionUID = -7775517952036303028L;

	protected Middleware middleware;

	protected Queue queue;

	protected DownloadTableModel model;

	protected String name;

	protected JTable table;

	private JPopupMenu dropDownMenu;

	private JMenuItem dropDownItem;

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

		dropDownItem = new JMenuItem("Start");
		dropDownItem.setEnabled(true);
		dropDownItem.setActionCommand("start");
		dropDownItem.addActionListener(new DropDownListener());
		this.dropDownMenu.add(dropDownItem);

		dropDownItem = new JMenuItem("Stop");
		dropDownItem.setEnabled(true);
		dropDownItem.setActionCommand("stop");
		dropDownItem.addActionListener(new DropDownListener());
		this.dropDownMenu.add(dropDownItem);

		dropDownItem = new JMenuItem("Abbrechen");
		dropDownItem.setActionCommand("cancel");
		dropDownItem.addActionListener(new DropDownListener());
		this.dropDownMenu.add(dropDownItem);

		table.addMouseListener(new MouseListener());

		this.setViewportView(table);
	}

	public String getHoster() {
		return name;
	}

	public Component getComponent() {
		return this;
	}

	public void update(Observable sender, Object message) {
		if ("queue".equals(message)) {
			model.newData();
		} else {
			Download download = (Download) message;
			model.updateData(download.getIndex());
		}
	}

	private class MouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == 3) {
				if (dropDownMenu.isVisible()) {
					dropDownMenu.setVisible(false);
				} else {
					dropDownMenu.setLocation(e.getLocationOnScreen());
					dropDownMenu.setVisible(true);
				}
			} else {
				dropDownMenu.setVisible(false);
			}
		}
	}

	private class DropDownListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String source = e.getActionCommand();
			System.out.println("'" + source + "' performed");
			if ("cancel".equals(source)) {
				dropDownMenu.setVisible(false);
				queue.removeDownload(table.getSelectedRow());
			} else if ("start".equals(source)) {
				dropDownMenu.setVisible(false);
				queue.startDownload(table.getSelectedRow());
			} else if ("stop".equals(source)) {
				dropDownMenu.setVisible(false);
				queue.stopDownload(table.getSelectedRow());
			}
		}

	}

}
