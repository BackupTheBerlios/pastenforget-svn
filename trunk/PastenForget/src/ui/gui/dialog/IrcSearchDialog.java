package ui.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import middleware.Tools;
import settings.Languages;
import ui.gui.GUI;
import download.DownloadTools;
import filtration.Packetnews;
import filtration.RequestPackage;

public class IrcSearchDialog extends JDialog implements ActionListener,
		Observer {

	private static final long serialVersionUID = -8357043899768903230L;

	private GUI gui;

	private JScrollPane scrollPane;

	private JTable table;

	private JPanel panel;

	private JLabel label;

	private JTextField textField;

	private JButton button;

	private Dimension windowSize = Dialog.getWindowsSizeIrcSearch();

	private Dimension labelSize = Dialog.getLabelSizeMedium();

	private Dimension textFieldSize = Dialog.getTextFieldSizeBig();

	private Dimension buttonSize = Dialog.getButtonSizeMedium();

	private List<RequestPackage> entries = new ArrayList<RequestPackage>();

	private Packetnews news = null;

	private TableDataModel dmodel = new TableDataModel();

	Container c;

	public IrcSearchDialog(GUI gui) {
		super(gui);
		this.gui = gui;
		this.setTitle(Languages.getTranslation("search") + " (IRC Packetnews)");
		this.setResizable(true);
		this.setSize(windowSize);
		this.setPreferredSize(windowSize);
		this.setLocation(Tools.getCenteredLocation(windowSize));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		c = this.getContentPane();
		c.setLayout(new BorderLayout());

		init();
	}

	private void init() {
		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		label = new JLabel(Languages.getTranslation("searchwords") + ":");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);
		panel.add(label);

		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setSize(textFieldSize);
		textField.setPreferredSize(textFieldSize);
		textField.setVisible(true);
		panel.add(textField);

		button = new JButton(Languages.getTranslation("search"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("search");
		button.addActionListener(this);
		button.setVisible(true);
		panel.add(button);

		button = new JButton(Languages.getTranslation("stop"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("stop");
		button.addActionListener(this);
		button.setVisible(true);
		panel.add(button);

		panel.setVisible(true);
		this.add(panel, BorderLayout.NORTH);

		scrollPane = new JScrollPane();
		table = new JTable(dmodel);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setFillsViewportHeight(true);
		scrollPane.add(table);

		scrollPane.setViewportView(table);
		scrollPane.setVisible(true);
		this.add(scrollPane, BorderLayout.CENTER);

		panel = new JPanel();

		button = new JButton(Languages.getTranslation("download"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("download");
		button.addActionListener(this);
		button.setVisible(true);
		panel.add(button);

		button = new JButton(Languages.getTranslation("close"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("close");
		button.addActionListener(this);
		button.setVisible(true);
		panel.add(button);

		panel.setVisible(true);
		this.add(panel, BorderLayout.SOUTH);

		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("search".equals(source)) {
			if (news != null) {
				news.cancel();
				entries = new ArrayList<RequestPackage>();
				dmodel.fireTableDataChanged();
			}
			news = new Packetnews(textField.getText());
			news.addObserver(this);
			new Thread(news).start();
		} else if ("download".equals(source)) {
			int[] rows = table.getSelectedRows();
			RequestPackage requestPackage = null;
			for (int i : rows) {
				requestPackage = entries.get(i);
				DownloadTools.addDownload(requestPackage, settings.Settings
						.getDownloadDirectory());
			}
		} else if ("stop".equals(source)) {
			if (news != null) {
				news.cancel();
			}
		} else if ("close".equals(source)) {
			if (news != null) {
				news.cancel();
			}
			this.dispose();
		}
	}

	private class TableDataModel extends AbstractTableModel {

		private static final long serialVersionUID = -7804198019362646369L;

		private final String[] columnIdentifiers = new String[] { "Server",
				"Channel", "Bot", Languages.getTranslation("package"),
				Languages.getTranslation("slots"),
				Languages.getTranslation("description") };

		public TableDataModel() {
			super();
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnIdentifiers[columnIndex];
		}

		@Override
		public int getColumnCount() {
			return columnIdentifiers.length;
		}

		@Override
		public int getRowCount() {
			return entries.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if ((rowIndex > -1) && (rowIndex + 1 <= getRowCount())
					&& (columnIndex < getColumnCount())) {
				RequestPackage entry = entries.get(rowIndex);
				switch (columnIndex) {
				case 0:
					return entry.getIrcServer();
				case 1:
					return entry.getIrcChannel();
				case 2:
					return entry.getBotName();
				case 3:
					return entry.getPackage();
				case 4:
					return entry.getSlots();
				case 5:
					return entry.getDescription();
				default:
					return null;
				}
			} else {
				return null;
			}
		}

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		RequestPackage entry = null;
		try {
			entry = (RequestPackage) arg1;
			entries.add(entry);
			dmodel.fireTableDataChanged();
		} catch (Exception e) {
			System.out.println("Search (IRC): failure");
		}
	}
}
