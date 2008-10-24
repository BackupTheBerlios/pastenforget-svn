package ui.gui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class DownloadDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -7459402167878262668L;

	private GUI gui;

	private JPanel panel;

	private JLabel label;

	private JTextField textField;

	private JButton confirm, cancel;

	private Dimension windowSize = Dialog.getWindowsSizeSmall();

	private Dimension labelSize = Dialog.getLabelSizeSmall();

	private Dimension textFieldSize = Dialog.getTextFieldSizeMedium();

	private Dimension buttonSize = Dialog.getButtonSizeMedium();

	Container c;

	public DownloadDialog(GUI gui) {
		super(gui, Languages.getTranslation("download"));
		this.gui = gui;

		this.setResizable(false);
		this.setSize(windowSize);
		this.setPreferredSize(windowSize);
		this.setLocation(Tools.getCenteredLocation(windowSize));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		c = this.getContentPane();
		c.setLayout(new GridLayout(2, 1, 10, 10));

		init();
	}

	private void init() {
		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		label = new JLabel(Languages.getTranslation("url") + ":");
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

		label = new JLabel("");
		label.setSize(buttonSize);
		label.setPreferredSize(buttonSize);
		label.setVisible(true);
		panel.add(label);

		panel.setVisible(true);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		c.add(panel);

		panel = new JPanel();

		confirm = new JButton(Languages.getTranslation("download"));
		confirm.setSize(buttonSize);
		confirm.setPreferredSize(buttonSize);
		confirm.setEnabled(true);
		confirm.setActionCommand("confirm");
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

		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("cancel".equals(source)) {
			this.dispose();
		} else if ("confirm".equals(source)) {
			try {
				gui.getMiddleware().download(new URL(this.textField.getText()));
			} catch (MalformedURLException e1) {
				System.out.println("Start download: wrong URL format");
				e1.printStackTrace();
			}
			this.dispose();
		}
	}
}
