package gui;

import gui.dialogs.DownloadDialog;
import gui.dialogs.MultiDownloadDialog;
import gui.dialogs.SettingsDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 8475301015978951356L;
	
	private GUI gui;

	public ToolBar(GUI gui) {
		this.gui = gui;
		init();
	}

	protected void init() {
		JButton button;

		button = new JButton(new ImageIcon("images/download.png"));
		button.setToolTipText("Neuer Download");
		button.addActionListener(this);
		button.setActionCommand("download");
		this.add(button);

		button = new JButton(new ImageIcon("images/multidownload.png"));
		button.setToolTipText("Multidownload");
		button.addActionListener(this);
		button.setActionCommand("multidownload");
		this.add(button);

		button = new JButton(new ImageIcon("images/settings.png"));
		button.setToolTipText("Einstellungen");
		button.addActionListener(this);
		button.setActionCommand("settings");
		this.add(button);
	}

	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("download".equals(source)) {
			new DownloadDialog(gui);
		} else if ("multidownload".equals(source)) {
			new MultiDownloadDialog(gui);
		} else if ("settings".equals(source)) {
			new SettingsDialog(gui);
		}
	}

}
