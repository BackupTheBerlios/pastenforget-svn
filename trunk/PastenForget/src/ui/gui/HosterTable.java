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

import middleware.ObserverMessageObject;
import queue.Queue;
import settings.Languages;
import ui.gui.dialog.CaptchaDialog;
import download.Download;
import download.HosterEnum;

/**
 * Gemeinsame GUI-Attribute aller Hoster.
 * 
 * @author executor
 * 
 */
public class HosterTable extends JScrollPane implements Observer {

	private static final long serialVersionUID = -7775517952036303028L;

	private GUI gui;

	private Queue queue;

	private DownloadTableDataModel dmodel;

	private DownloadTableColumnModel cmodel;

	private String name;

	private JTable table;

	private JPopupMenu dropDownMenu;

	private JMenuItem dropDownItem;

	public HosterTable(GUI gui, HosterEnum hoster) {
		this.gui = gui;
		this.name = hoster.getName();

		queue = gui.getMiddleware().getQueue(hoster.getName());
		queue.addObserver(this);

		dmodel = new DownloadTableDataModel(queue);
		cmodel = new DownloadTableColumnModel();

		table = new JTable(dmodel, cmodel);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setFillsViewportHeight(true);

		this.dropDownMenu = new JPopupMenu();
		this.dropDownMenu.setEnabled(true);
		dropDownItem = new JMenuItem(Languages.getTranslation("start"));
		dropDownItem.setEnabled(true);
		dropDownItem.setActionCommand("start");
		dropDownItem.addActionListener(new DropDownListener());
		this.dropDownMenu.add(dropDownItem);

		dropDownItem = new JMenuItem(Languages.getTranslation("stop"));
		dropDownItem.setEnabled(true);
		dropDownItem.setActionCommand("stop");
		dropDownItem.addActionListener(new DropDownListener());
		this.dropDownMenu.add(dropDownItem);

		dropDownItem = new JMenuItem(Languages.getTranslation("cancel"));
		dropDownItem.setActionCommand("cancel");
		dropDownItem.addActionListener(new DropDownListener());
		this.dropDownMenu.add(dropDownItem);

		table.addMouseListener(new MouseListener());

		this.add(table);
		this.setViewportView(table);
	}

	public String getHoster() {
		return name;
	}

	public Component getComponent() {
		return this;
	}

	public void update(Observable sender, Object message) {
		if (message.getClass() == ObserverMessageObject.class) {
			ObserverMessageObject omo = (ObserverMessageObject) message;
			if (omo.isQueue()) {
				dmodel.fireTableDataChanged();
			} else if (omo.isDownload() && !omo.isCaptcha()) {
				Download download = omo.getDownload();
				int i = this.queue.getDownloadList().indexOf(download);
				dmodel.fireTableRowsUpdated(i, i);
			} else if (omo.isDownload() && omo.isCaptcha())  {
				new CaptchaDialog(gui, omo.getDownload());
			}
		}
	}

	private class MouseListener extends MouseAdapter {

		@Override
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
				if (table.getSelectedRows().length > 0) {
					queue.removeDownloads(table.getSelectedRows());
				} 
			} else if ("start".equals(source)) {
				dropDownMenu.setVisible(false);
				queue.startDownloads(table.getSelectedRows());
			} else if ("stop".equals(source)) {
				dropDownMenu.setVisible(false);
				if (table.getSelectedRows().length > 0) {
					queue.stopDownloads(table.getSelectedRows());
				}
			}
		}

	}

}
