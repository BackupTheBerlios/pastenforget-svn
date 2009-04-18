package ui.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import middleware.Middleware;
import middleware.ObserverMessageObject;
import queue.Queue;
import settings.Languages;
import download.Download;
import download.HosterEnum;
import download.Status;

public class Log extends JScrollPane {

	private static final long serialVersionUID = -2019686782925190876L;

	private JTable table;

	private LogTableDataModel dmodel;

	private JPopupMenu dropDownMenu;

	private JMenuItem dropDownItem;

	public Log(GUI gui) {
		this.setMinimumSize(new Dimension(640, 100));
		this.setPreferredSize(new Dimension(640, 200));
		this.setSize(new Dimension(640, 200));

		init();
	}

	private void init() {
		dmodel = new LogTableDataModel();
		table = new JTable(dmodel);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setFillsViewportHeight(true);
		table.addMouseListener(new MouseListener());

		dropDownMenu = new JPopupMenu();
		dropDownItem = new JMenuItem(Languages.getTranslation("ClearList"));
		dropDownItem.setEnabled(true);
		dropDownItem.setActionCommand("clear");
		dropDownItem.addActionListener(new DropDownListener());
		dropDownMenu.add(dropDownItem);

		this.add(table);
		this.setViewportView(table);
	}

	private class LogTableDataModel extends AbstractTableModel implements
			Observer {

		public LogTableDataModel() {
			Queue queue;
			for (HosterEnum hoster : HosterEnum.values()) {
				queue = Middleware.getQueue(hoster.getName());
				queue.addObserver(this);
			}
		}

		private static final long serialVersionUID = -7334216780241561897L;

		private final String[] columnIdentifiers = new String[] {
				Languages.getTranslation("Time"),
				Languages.getTranslation("Hoster"),
				Languages.getTranslation("Download"),
				Languages.getTranslation("Message") };

		private List<Vector<String>> logs = new LinkedList<Vector<String>>();

		@Override
		public int getColumnCount() {
			return columnIdentifiers.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnIdentifiers[columnIndex];
		}

		@Override
		public int getRowCount() {
			return logs.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			try {
				return logs.get(rowIndex).get(columnIndex);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public void update(Observable sender, Object message) {
			if (logs.size() >= 100) {
				for (int i = 0; i < 5; i++) {
					logs.remove(0);
				}
			}
			
			if (message.getClass() == ObserverMessageObject.class) {
				ObserverMessageObject omo = (ObserverMessageObject) message;
				
				if (omo.isDownload()) {
					Download download = omo.getDownload();
					String status = download.getStatus();
					if (Status.getStarted().equals(status)
							|| Status.getCanceled().equals(status)
							|| Status.getFinished().equals(status)
							|| Status.getStopped().equals(status)
							|| (status.indexOf(Languages.getTranslation("Error")) != -1)) {

						String time = new SimpleDateFormat(
								"yyyy'-'MM'-'dd': 'HH:mm:ss")
								.format(new Date());
						String hoster = "";
						for (HosterEnum host : HosterEnum.values()) {
							if (download.getUrl().toString().matches(host.getPattern())) {
								hoster = host.getName();
								break;
							}
						}
						Vector<String> log = new Vector<String>();
						log.add(time);
						log.add(hoster);
						log.add(download.getFileName());
						log.add(status);
						logs.add(log);
						this.fireTableDataChanged();
					}
					
				}
			}
		}

		public void clear() {
			logs.clear();
			this.fireTableDataChanged();
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
			if ("clear".equals(source)) {
				dropDownMenu.setVisible(false);
				dmodel.clear();
			}
		}
	}

}
