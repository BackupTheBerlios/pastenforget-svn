package gui.dialogs;

import gui.GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Dialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -1638924234468331886L;

	protected GUI gui;

	protected JLabel label;

	protected JTextField textField;

	protected JButton confirm, cancel, path;

	Container c;

	public Dialog() {
		c = this.getContentPane();
		c.setLayout(new GridLayout(2,1,10,10));

		this.setSize(600, 150);
		this.setLocation(new Point(150, 150));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(500, 25));

		label = new JLabel("");
		label.setSize(80, 25);
		label.setVisible(true);

		panel.add(label);

		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setSize(300, 25);
		textField.setPreferredSize(new Dimension(300, 25));
		textField.setVisible(true);

		panel.add(textField);

		path = new JButton("Pfad");
		path.setSize(120, 25);
		path.setEnabled(true);
		path.setActionCommand("path");
		path.addActionListener(this);
		path.setVisible(false);

		panel.add(path);

		c.add(panel);

		panel = new JPanel();

		confirm = new JButton("Download");
		confirm.setSize(120, 25);
		confirm.setEnabled(true);
		confirm.setActionCommand("confirm");
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

		c.add(panel);

		this.pack();
		this.setVisible(true);
	}

	// To override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("cancel".equals(source)) {
			this.dispose();
		}
	}
}
