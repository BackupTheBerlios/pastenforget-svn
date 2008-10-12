package ui.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import middleware.Middleware;
import middleware.Tools;
import queue.Queue;
import download.Download;
import download.Status;
import download.hoster.HosterEnum;

public class Log extends JScrollPane {

	private static final long serialVersionUID = -2019686782925190876L;

	private Middleware middleware;

	private JTable table;

	public Log(GUI gui) {
		this.middleware = gui.getMiddleware();

		init();
	}

	private void init() {
		table = new JTable(new LogTableDataModel());
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setFillsViewportHeight(true);

		this.setViewportView(table);
	}

	private class LogTableDataModel extends AbstractTableModel implements
			Observer {

		public LogTableDataModel() {
			Queue queue;
			for (HosterEnum hoster : HosterEnum.values()) {
				queue = middleware.getQueue(hoster.getKey());
				queue.addObserver(this);
			}
		}

		private static final long serialVersionUID = -7334216780241561897L;

		private final String[] columnIdentifiers = new String[] { "Zeit",
				"Hoster", "Download", "Meldung" };

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
			if (message.getClass().getSuperclass() == Download.class) {
				Download download = (Download) message;
				String status = download.getStatus();
				if (Status.getActive().equals(status)
						|| Status.getCanceled().equals(status)
						|| Status.getFinished().equals(status)
						|| Status.getStopped().equals(status)
						|| (status.indexOf("Fehler") != -1)) {

					String time = new SimpleDateFormat(
							"yyyy'-'MM'-'dd': 'HH:mm:ss' Uhr'")
							.format(new Date());
					int key = Tools.checkHoster(download.getUrl().toString());
					String hoster = "";
					for (HosterEnum host : HosterEnum.values()) {
						if (host.getKey() == key) {
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

}
