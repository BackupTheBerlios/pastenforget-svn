package ui.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import middleware.Tools;
import searchWebsite.SearchEntry;
import searchWebsite.SearchWebsite;
import searchWebsite.SearchWebsiteEnum;
import settings.Languages;
import ui.gui.GUI;
import download.DownloadTools;

/**
 * Dialog für eine Suchanfrage.
 * 
 * @author executor
 * 
 */
public class SearchDialog extends JDialog implements ActionListener, Observer {

	private static final long serialVersionUID = -8357043899768903230L;

	private JScrollPane scrollPane;

	private JTable table;

	private JTextField textField;

	private JList list;

	private JButton button;

	private Dimension windowSize = Dialog.getWindowsSizeIrcSearch();

	private Dimension labelSize = Dialog.getLabelSizeMedium();

	private Dimension textFieldSize = Dialog.getTextFieldSizeBig();

	private Dimension buttonSize = Dialog.getButtonSizeMedium();

	private Vector<SearchWebsite> searchWebsites = new Vector<SearchWebsite>();

	private List<SearchEntry> entries = new ArrayList<SearchEntry>();

	private TableDataModel dmodel = new TableDataModel();

	Container c;

	public SearchDialog(GUI gui) {
		super(gui);
		this.setTitle(Languages.getTranslation("search"));
		this.setResizable(true);
		this.setSize(windowSize);
		this.setPreferredSize(windowSize);
		this.setMinimumSize(windowSize);
		this.setLocation(Tools.getCenteredLocation(windowSize));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		c = this.getContentPane();
		c.setLayout(new BorderLayout());

		init();
	}

	private void init() {
		JPanel panelNorth = new JPanel(new FlowLayout(FlowLayout.CENTER));

		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panelTemp = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelTemp.setVisible(true);

		JLabel label = new JLabel(Languages.getTranslation("searchwords") + ":");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);
		panelTemp.add(label);

		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setSize(textFieldSize);
		textField.setPreferredSize(textFieldSize);
		textField.setVisible(true);
		panelTemp.add(textField);

		panel.add(panelTemp);

		panelTemp = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelTemp.setVisible(true);

		button = new JButton(Languages.getTranslation("search"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("search");
		button.addActionListener(this);
		button.setVisible(true);
		panelTemp.add(button);

		button = new JButton(Languages.getTranslation("stop"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("stop");
		button.addActionListener(this);
		button.setVisible(true);
		panelTemp.add(button);

		button = new JButton(Languages.getTranslation("select") + " "
				+ Languages.getTranslation("all") + "/"
				+ Languages.getTranslation("none"));
		button.setSize(buttonSize);
		button.setPreferredSize(Dialog.getButtonSizeBig());
		button.setEnabled(true);
		button.setActionCommand("select");
		button.addActionListener(this);
		button.setVisible(true);
		panelTemp.add(button);

		panel.add(panelTemp);
		panelNorth.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.setVisible(true);

		list = new JList();
		list.setBorder(new TitledBorder(Languages
				.getTranslation("searchwebsites")));
		list.setPreferredSize(new Dimension(180, 100));

		SearchWebsite swebsite = null;
		for (SearchWebsiteEnum sw : SearchWebsiteEnum.values()) {
			Class<?> myClass = null;
			try {
				myClass = Class.forName(sw.getClassName());
				swebsite = (SearchWebsite) myClass.newInstance();
				searchWebsites.add(swebsite);
				swebsite.addObserver(this);
			} catch (Exception e) {
				System.out.println("SearchDialog: failure");
			}
		}

		list.setListData(searchWebsites);
		list.setSelectedIndex(0);
		list.setEnabled(true);
		panel.add(list);

		panelNorth.add(panel);

		this.add(panelNorth, BorderLayout.NORTH);

		scrollPane = new JScrollPane();
		scrollPane.setVisible(true);
		table = new JTable(dmodel);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setFillsViewportHeight(true);
		scrollPane.add(table);
		scrollPane.setViewportView(table);

		this.add(scrollPane, BorderLayout.CENTER);

		panel = new JPanel();
		panel.setVisible(true);

		button = new JButton(Languages.getTranslation("download"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("download");
		button.addActionListener(this);
		button.setVisible(true);
		panel.add(button);

		button = new JButton(Languages.getTranslation("details"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("details");
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

		this.add(panel, BorderLayout.SOUTH);

		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("search".equals(source)) {
			for (SearchWebsite swebsite : searchWebsites) {
				swebsite.stopSearch();
			}
			this.entries.clear();
			dmodel.fireTableDataChanged();

			int[] swIndices = list.getSelectedIndices();
			for (int i : swIndices) {
				try {
					this.searchWebsites.get(i).search(textField.getText());
				} catch (IOException e1) {
					System.out.println("SearchDialog.actionPerforme: failure");
				}
			}
		} else if ("stop".equals(source)) {
			for (SearchWebsite swebsite : searchWebsites) {
				swebsite.stopSearch();
			}
		} else if ("select".equals(source)) {
			if (list.getSelectedIndices() == null) {
				for (int i = 0; i < list.getMaxSelectionIndex(); i++) {
					list.setSelectedIndex(i);
				}
			} else {
				list.setSelectedIndex(-1);
			}
		} else if ("download".equals(source)) {
			int[] rows = table.getSelectedRows();
			SearchEntry searchEntry = null;
			for (int i : rows) {
				searchEntry = entries.get(i);
				this.download(searchEntry);
			}
		} else if ("details".equals(source)) {
			int[] rows = table.getSelectedRows();
			SearchEntry searchEntry = null;
			for (int i : rows) {
				searchEntry = entries.get(i);
				searchEntry.showDetails();
			}
		} else if ("close".equals(source)) {
			for (SearchWebsite swebsite : searchWebsites) {
				swebsite.stopSearch();
			}
			this.dispose();
		}
	}

	private void download(SearchEntry searchEntry) {
		try {
			List<URL> urls = searchEntry.getLinks();
			for (URL url : urls) {
				DownloadTools.addDownload(url, settings.Settings
						.getDownloadDirectory());
			}
		} catch (IOException e) {
			System.out.println("SearchDialog.download: failure");
		}
	}

	private class TableDataModel extends AbstractTableModel {

		private static final long serialVersionUID = -7804198019362646369L;

		private final String[] columnIdentifiers = new String[] {
				Languages.getTranslation("name"),
				Languages.getTranslation("date"),
				Languages.getTranslation("website") };

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
			if ((rowIndex > -1) && (rowIndex < getRowCount())
					&& (columnIndex < getColumnCount())) {
				SearchEntry entry = entries.get(rowIndex);
				switch (columnIndex) {
				case 0:
					return entry.getName();
				case 1:
					return entry.getDate();
				case 2:
					return entry.getWebsite();
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
		SearchEntry entry = null;
		try {
			entry = (SearchEntry) arg1;
			entries.add(entry);
			dmodel.fireTableDataChanged();
		} catch (Exception e) {
			System.out.println("SearchDialog: failure");
		}
	}
}
