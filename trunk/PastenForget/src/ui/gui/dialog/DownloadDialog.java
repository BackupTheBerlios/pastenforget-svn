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
import download.DownloadTools;

public class DownloadDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -7459402167878262668L;

	private JTextField textField;

	private Dimension windowSize = Dialog.getWindowsSizeSmall();

	private Dimension labelSize = Dialog.getLabelSizeSmall();

	private Dimension textFieldSize = Dialog.getTextFieldSizeBig();

	private Dimension buttonSize = Dialog.getButtonSizeMedium();

	Container c;

	public DownloadDialog(GUI gui) {
		super(gui, Languages.getTranslation("Download"));

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
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.setVisible(true);

		JLabel label = new JLabel(Languages.getTranslation("URL") + ":");
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

		c.add(panel);

		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.setVisible(true);

		JButton button = new JButton(Languages.getTranslation("Cancel"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("cancel");
		button.addActionListener(this);
		button.setVisible(true);
		panel.add(button);
		
		button = new JButton(Languages.getTranslation("Download"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("confirm");
		button.addActionListener(this);
		button.setVisible(true);
		panel.add(button);

		c.add(panel);

		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		if ("cancel".equals(source)) {
			this.dispose();
		} else if ("confirm".equals(source)) {
			try {
				DownloadTools.addDownload(new URL(this.textField.getText()),
						settings.Settings.getDownloadDirectory());
			} catch (MalformedURLException e1) {
				System.out.println("DownloadDialog: wrong URL format");
			}
			this.dispose();
		}
	}
}
