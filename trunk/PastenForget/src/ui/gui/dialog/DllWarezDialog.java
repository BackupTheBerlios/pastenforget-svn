package ui.gui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import middleware.Tools;
import settings.Languages;
import ui.gui.GUI;
import filtration.MirrorSearch;

public class DllWarezDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -8357043899768903230L;

	private JPanel panel;

	private JLabel label, labelPath;

	private JTextField textField, path;

	private JButton confirm, cancel, search;

	private Dimension windowSize = Dialog.getWindowsSizeMedium();

	private Dimension labelSize = Dialog.getLabelSizeMedium();

	private Dimension textFieldSize = Dialog.getTextFieldSizeBig();

	private Dimension buttonSize = Dialog.getButtonSizeMedium();

	private File destination = null;

	private URL url = null;

	Container c;

	public DllWarezDialog(GUI gui) {
		super(gui);
		this.destination = settings.Settings.getSrcDirectory();

		this.setResizable(false);
		this.setSize(windowSize);
		this.setPreferredSize(windowSize);
		this.setLocation(Tools.getCenteredLocation(windowSize));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		c = this.getContentPane();
		c.setLayout(new GridLayout(3, 1, 10, 10));

		init();
	}

	private void init() {
		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		labelPath = new JLabel(Languages.getTranslation("destination") + ":");
		labelPath.setSize(labelSize);
		labelPath.setPreferredSize(labelSize);
		labelPath.setVisible(true);
		panel.add(labelPath);

		path = new JTextField();
		if (settings.Settings.getSrcDirectory() != null) {
			destination = settings.Settings.getSrcDirectory();
			path.setText(settings.Settings.getSrcDirectory().toString());
		}
		path.setBackground(Color.WHITE);
		path.setSize(textFieldSize);
		path.setPreferredSize(textFieldSize);
		path.setVisible(true);
		panel.add(path);

		search = new JButton(Languages.getTranslation("search"));
		search.setSize(buttonSize);
		search.setPreferredSize(buttonSize);
		search.setEnabled(true);
		search.setActionCommand("path");
		search.addActionListener(this);
		search.setVisible(true);
		panel.add(search);

		panel.setVisible(true);
		c.add(panel);

		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		label = new JLabel();
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

		JLabel emptyLabel = new JLabel();
		emptyLabel.setSize(buttonSize);
		emptyLabel.setPreferredSize(buttonSize);
		emptyLabel.setVisible(true);
		panel.add(emptyLabel);

		panel.setVisible(true);

		c.add(panel);

		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		confirm = new JButton();
		confirm.setSize(buttonSize);
		confirm.setPreferredSize(buttonSize);
		confirm.setEnabled(true);
		confirm.addActionListener(this);
		confirm.setVisible(true);
		panel.add(confirm);

		cancel = new JButton(Languages.getTranslation("cancel"));
		cancel.setSize(buttonSize);
		cancel.setPreferredSize(buttonSize);
		cancel.setEnabled(true);
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		cancel.setVisible(true);
		panel.add(cancel);

		panel.setVisible(true);

		c.add(panel);
	}

	public void search() {
		this.setTitle(Languages.getTranslation("search") + " (DDL-Warez)");

		label.setText(Languages.getTranslation("searchwords") + ":");
		confirm.setText(Languages.getTranslation("start"));
		confirm.setActionCommand("search");

		this.setVisible(true);
		this.pack();
	}

	public void filter() {
		this.setTitle(Languages.getTranslation("filter") + " URLs (DDL-Warez)");

		label.setText("URL (DDL-Warez):");
		confirm.setText(Languages.getTranslation("filter"));
		confirm.setActionCommand("filter");

		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("path".equals(source)) {
			destination = new PathDialog(path.getText()).getDestination();
			if (destination != null) {
				path.setText(destination.getPath());
			}
		} else if ("cancel".equals(source)) {
			this.dispose();
		} else if ("filter".equals(source)) {
			try {
				url = new URL(this.textField.getText());
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			if (url != null && destination != null
					&& !"".equals(path.getText())) {
				try {
					MirrorSearch.filterMirrors(url, new File(path.getText()));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				this.dispose();
			} else {
				System.out.println("Filter URLs: no URL or path");
			}
		} else if ("search".equals(source)) {
			if (destination != null && !"".equals(textField.getText())
					&& !"".equals(path.getText())) {
				try {
					MirrorSearch.search(textField.getText(), new File(path
							.getText()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				this.dispose();
			} else {
				System.out.println("Search: no keywords or path");
			}

		}
	}
}
