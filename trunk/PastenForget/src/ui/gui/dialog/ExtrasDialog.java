package ui.gui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
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

import ui.gui.GUI;
import filtration.MirrorSearch;

public class ExtrasDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -8357043899768903230L;

	private GUI gui;

	private JPanel panel;

	private JLabel label;

	private JTextField textField, path;

	private JButton confirm, cancel, search;

	private File destination = null;

	private URL url = null;
	
	private middleware.Settings settings = null;

	Container c;

	public ExtrasDialog(GUI gui) {
		this.gui = gui;
		this.settings = this.gui.getMiddleware().getSettings();

		this.c = this.getContentPane();
		this.setLocation(new Point(150, 150));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		c.setLayout(new GridLayout(3, 1, 10, 10));

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(550, 40));

		JLabel pathLabel = new JLabel("Speicherort:");
		pathLabel.setPreferredSize(new Dimension(150, 25));
		pathLabel.setVisible(true);

		panel.add(pathLabel);

		path = new JTextField();
		if (settings.getDestinationDllwarez() != null) {
			destination = settings.getDestinationDllwarez();
			path.setText(settings.getDestinationDllwarez().toString());
		}
		path.setBackground(Color.WHITE);
		path.setSize(300, 25);
		path.setPreferredSize(new Dimension(300, 25));
		path.setVisible(true);

		panel.add(path);

		search = new JButton("Suchen");
		search.setSize(120, 25);
		search.setEnabled(true);
		search.setActionCommand("path");
		search.addActionListener(this);
		search.setVisible(true);

		panel.add(search);

		panel.setVisible(true);
		c.add(panel);

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(550, 40));

		label = new JLabel();
		label.setPreferredSize(new Dimension(150, 25));
		label.setVisible(true);
		panel.add(label);

		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setSize(300, 25);
		textField.setPreferredSize(new Dimension(300, 25));
		textField.setVisible(true);
		panel.add(textField);

		JLabel emptyLabel = new JLabel();
		emptyLabel.setPreferredSize(new Dimension(60, 25));
		emptyLabel.setVisible(true);
		panel.add(emptyLabel);

		panel.setVisible(true);
		c.add(panel);

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(550, 40));

		confirm = new JButton();
		confirm.setSize(120, 25);
		confirm.setEnabled(true);
		confirm.addActionListener(this);
		confirm.setVisible(true);

		panel.add(confirm);

		cancel = new JButton("Abbrechen");
		cancel.setSize(120, 25);
		cancel.setEnabled(true);
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		cancel.setVisible(true);
		panel.add(cancel);

		panel.setVisible(true);
		c.add(panel);
	}

	public void search() {
		this.setTitle("Suche (DDL-Warez)");

		label.setText("Suchbegriffe:");
		confirm.setText("Start");
		confirm.setActionCommand("search");

		this.setVisible(true);
		this.pack();
	}

	public void filter() {
		this.setTitle("Filter URLs (DDL-Warez)");

		label.setText("URL (DDL-Warez):");
		confirm.setText("Filter");
		confirm.setActionCommand("filter");

		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("path".equals(source)) {
			destination = new PathDialog().getDestination();
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
			if (url != null && destination != null) {
				try {
					MirrorSearch.filterMirrors(url, destination);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				this.dispose();
			} else {
				System.out.println("Filter URLs: no URL or path");
			}
		} else if ("search".equals(source)) {
			if (destination != null && !"".equals(textField.getText())) {
				try {
					MirrorSearch.search(textField.getText(), destination);
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
