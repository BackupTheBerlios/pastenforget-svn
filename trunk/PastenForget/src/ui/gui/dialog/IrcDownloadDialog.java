package ui.gui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import middleware.Tools;
import settings.Languages;
import ui.gui.GUI;
import download.DownloadTools;
import filtration.RequestPackage;

public class IrcDownloadDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -8357043899768903230L;

	private JTextField server, channel, bot, pack;

	private Dimension windowSize = Dialog.getWindowsSizeIrcDownload();

	private Dimension labelSize = Dialog.getLabelSizeBig();

	private Dimension textFieldSize = Dialog.getTextFieldSizeMedium();

	private Dimension buttonSize = Dialog.getButtonSizeMedium();

	Container c;

	public IrcDownloadDialog(GUI gui) {
		super(gui);
		
		this.setTitle(Languages.getTranslation("Download") + " (IRC)");
		this.setResizable(false);
		this.setSize(windowSize);
		this.setPreferredSize(windowSize);
		this.setLocation(Tools.getCenteredLocation(windowSize));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		c = this.getContentPane();
		c.setLayout(new GridLayout(5, 1, 5, 5));

		init();
	}

	private void init() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.setVisible(true);

		JLabel label = new JLabel("Server:");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);
		panel.add(label);

		server = new JTextField();
		server.setBackground(Color.WHITE);
		server.setSize(textFieldSize);
		server.setPreferredSize(textFieldSize);
		server.setVisible(true);
		panel.add(server);

		this.add(panel);

		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.setVisible(true);

		label = new JLabel("Channel:");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);
		panel.add(label);

		channel = new JTextField();
		channel.setBackground(Color.WHITE);
		channel.setSize(textFieldSize);
		channel.setPreferredSize(textFieldSize);
		channel.setVisible(true);
		panel.add(channel);

		this.add(panel);

		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.setVisible(true);

		label = new JLabel("Bot:");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);
		panel.add(label);

		bot = new JTextField();
		bot.setBackground(Color.WHITE);
		bot.setSize(textFieldSize);
		bot.setPreferredSize(textFieldSize);
		bot.setVisible(true);
		panel.add(bot);

		this.add(panel);

		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.setVisible(true);

		label = new JLabel(Languages.getTranslation("Package") + ":");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);
		panel.add(label);

		pack = new JTextField();
		pack.setBackground(Color.WHITE);
		pack.setSize(textFieldSize);
		pack.setPreferredSize(textFieldSize);
		pack.setVisible(true);
		panel.add(pack);

		this.add(panel);

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
		button.setActionCommand("download");
		button.addActionListener(this);
		button.setVisible(true);
		panel.add(button);

		this.add(panel);

		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		if ("cancel".equals(source)) {
			this.dispose();
		} else if ("download".equals(source)) {
			if (checkFields()) {
				RequestPackage requestPackage = new RequestPackage("", "", "",
						"", "", server.getText(), channel.getText(), bot
								.getText(), pack.getText(), "", "");
				DownloadTools.addDownload(requestPackage, settings.Settings
						.getDownloadDirectory());
				this.dispose();
			}
		}
	}

	private boolean checkFields() {
		if ("".equals(server.getText()) || server.getText() == null) {
			return false;
		}
		if ("".equals(channel.getText()) || channel.getText() == null) {
			return false;
		}
		if ("".equals(bot.getText()) || bot.getText() == null) {
			return false;
		}
		if ("".equals(pack.getText()) || pack.getText() == null) {
			return false;
		}
		return true;
	}
}
