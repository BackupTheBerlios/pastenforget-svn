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

	protected GUI gui;

	protected JPanel panel;

	protected JLabel label;

	protected JTextField textField, path;

	protected JButton button;

	protected File destination = null;

	protected URL url = null;

	Container c;

	public ExtrasDialog(GUI gui) {
		this.gui = gui;

		this.c = this.getContentPane();
		this.setLocation(new Point(150, 150));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		c.setLayout(new GridLayout(3, 1, 10, 10));

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(550, 40));

		label = new JLabel("Speicherort:");
		label.setPreferredSize(new Dimension(150, 25));
		label.setVisible(true);

		panel.add(label);

		path = new JTextField();
		path.setBackground(Color.WHITE);
		path.setSize(300, 25);
		path.setPreferredSize(new Dimension(300, 25));
		path.setVisible(true);

		panel.add(path);

		button = new JButton("Suchen");
		button.setSize(120, 25);
		button.setEnabled(true);
		button.setActionCommand("path");
		button.addActionListener(this);
		button.setVisible(true);

		panel.add(button);

		panel.setVisible(true);
		c.add(panel);
	}

	public void search() {
		this.setTitle("Suche (DDL-Warez)");
		this.setTitle("Hilfe");
		this.setVisible(true);
		this.pack();
	}

	public void filter() {
		this.setTitle("Filter URLs (DDL-Warez)");

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(550, 40));

		label = new JLabel("URL (DDL-Warez):");
		label.setPreferredSize(new Dimension(150, 25));
		label.setVisible(true);
		panel.add(label);

		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setSize(300, 25);
		textField.setPreferredSize(new Dimension(300, 25));
		textField.setVisible(true);
		panel.add(textField);

		label = new JLabel();
		label.setPreferredSize(new Dimension(60, 25));
		label.setVisible(true);
		panel.add(label);

		panel.setVisible(true);
		c.add(panel);

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(550, 40));

		button = new JButton("Filter");
		button.setSize(120, 25);
		button.setEnabled(true);
		button.setActionCommand("filter");
		button.addActionListener(this);
		button.setVisible(true);

		panel.add(button);

		button = new JButton("Abbrechen");
		button.setSize(120, 25);
		button.setEnabled(true);
		button.setActionCommand("cancel");
		button.addActionListener(this);
		button.setVisible(true);
		panel.add(button);

		panel.setVisible(true);
		c.add(panel);

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

		}
	}
}
