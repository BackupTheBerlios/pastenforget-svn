package ui.gui.settings;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import middleware.Tools;
import settings.Languages;
import ui.gui.GUI;
import ui.gui.dialog.Dialog;

public class Settings extends JDialog implements ActionListener {

	private static final long serialVersionUID = -8357043899768903230L;

	private GUI gui;

	private JPanel panel;

	private JTabbedPane tpane;

	private JButton confirm, accept, cancel;

	private Dimension windowSize = Dialog.getWindowsSizeBig();

	private Dimension buttonSizeSmall = Dialog.getButtonSizeSmall();

	private Dimension buttonSizeBig = Dialog.getButtonSizeBig();

	private List<SettingsInterface> settingsList = new LinkedList<SettingsInterface>();

	Container c;

	public Settings(GUI gui) {
		super(gui, Languages.getTranslation("Settings"));
		this.gui = gui;

		this.setResizable(false);
		this.setSize(windowSize);
		this.setPreferredSize(windowSize);
		this.setLocation(Tools.getCenteredLocation(windowSize));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		c = this.getContentPane();
		c.setLayout(new BorderLayout());

		init();

		this.pack();
		this.setVisible(true);
	}

	private void init() {
		panel = new JPanel();

		tpane = new JTabbedPane();
		tpane.setPreferredSize(new Dimension(windowSize.width - 10,
				windowSize.height - 3*buttonSizeBig.height));
		SettingsInterface temp;
		for (SettingsEnum setting : SettingsEnum.values()) {
			temp = setting.getSetting(gui);
			settingsList.add(temp);
			tpane.add(temp.getComponent(), setting.getLabel());
		}
		panel.add(tpane);

		c.add(panel, BorderLayout.CENTER);

		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		confirm = new JButton(Languages.getTranslation("Confirm"));
		confirm.setSize(buttonSizeSmall);
		confirm.setPreferredSize(buttonSizeSmall);
		confirm.setEnabled(true);
		confirm.setActionCommand("confirm");
		confirm.addActionListener(this);
		confirm.setVisible(true);
		panel.add(confirm);

		accept = new JButton(Languages.getTranslation("Accept"));
		accept.setSize(buttonSizeBig);
		accept.setPreferredSize(buttonSizeBig);
		accept.setEnabled(true);
		accept.setActionCommand("accept");
		accept.addActionListener(this);
		accept.setVisible(true);
		panel.add(accept);

		cancel = new JButton(Languages.getTranslation("Cancel"));
		cancel.setSize(buttonSizeBig);
		cancel.setPreferredSize(buttonSizeBig);
		cancel.setEnabled(true);
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		cancel.setVisible(true);
		panel.add(cancel);

		panel.setVisible(true);

		c.add(panel, BorderLayout.SOUTH);

		this.pack();
		this.setVisible(true);
	}

	private void setSettings() {
		for (SettingsInterface setting : settingsList) {
			setting.accept();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("confirm".equals(source)) {
			this.setSettings();
			settings.Settings.save();
			this.dispose();
		} else if ("cancel".equals(source)) {
			this.dispose();
		} else if ("accept".equals(source)) {
			this.setSettings();
			settings.Settings.save();
		}
	}
}
