package ui.gui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import middleware.Tools;
import settings.Languages;
import ui.gui.GUI;
import download.DownloadTools;

public class RsdfDownloadDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -7459402167878262668L;

	private JPanel panel;

	private JLabel label;

	private JTextField textField;

	private JButton confirm, cancel, search;

	private Dimension windowSize = Dialog.getWindowsSizeSmall();

	private Dimension labelSize = Dialog.getLabelSizeSmall();

	private Dimension textFieldSize = Dialog.getTextFieldSizeBig();

	private Dimension buttonSize = Dialog.getButtonSizeMedium();

	Container c;

	private File file = null;

	public RsdfDownloadDialog(GUI gui) {
		super(gui, Languages.getTranslation("rsdfdownload"));
		
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

		label = new JLabel(Languages.getTranslation("file") + "( RSDF):");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);
		panel.add(label);

		textField = new JTextField();
		if (settings.Settings.getSrcDirectory() != null) {
			textField.setText(settings.Settings.getSrcDirectory().toString());
		}
		textField.setBackground(Color.WHITE);
		textField.setSize(textFieldSize);
		textField.setPreferredSize(textFieldSize);
		textField.setVisible(true);
		panel.add(textField);

		search = new JButton(Languages.getTranslation("search"));
		search.setSize(buttonSize);
		search.setPreferredSize(buttonSize);
		search.setEnabled(true);
		search.setActionCommand("path");
		search.addActionListener(this);
		search.setVisible(true);
		panel.add(search);

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
			DownloadTools.loadRsdf(file);
			this.dispose();
		} else if ("path".equals(source)) {
			file = new FileDialog(this.textField.getText()).getFile();
			if (file != null) {
				textField.setText(file.getPath());
			}
		}

	}
}
