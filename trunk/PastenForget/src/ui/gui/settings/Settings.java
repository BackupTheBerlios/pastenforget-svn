package ui.gui.settings;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import settings.Languages;
import ui.gui.GUI;

public class Settings extends JDialog implements ActionListener {

	private static final long serialVersionUID = -8357043899768903230L;

	private GUI gui;

	private JPanel panel;

	private JTabbedPane tpane;

	private JButton confirm, accept, cancel;

	private List<SettingsInterface> settingsList = new LinkedList<SettingsInterface>();

	Container c;

	public Settings(GUI gui) {
		this.gui = gui;

		this.c = this.getContentPane();
		this.c.setLayout(new BorderLayout());
		this.setLocation(new Point(150, 150));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setTitle(Languages.getTranslation("settings"));

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(550, 400));

		tpane = new JTabbedPane();
		tpane.setPreferredSize(new Dimension(550, 380));
		
		SettingsInterface temp;
		for (SettingsEnum setting : SettingsEnum.values()) {
			temp = setting.getSetting(gui);
			settingsList.add(temp);
			tpane.add(temp.getComponent(), setting.getLabel());
		}

		panel.add(tpane);

		c.add(panel, BorderLayout.CENTER);

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(550, 40));
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		confirm = new JButton(Languages.getTranslation("confirm"));
		confirm.setSize(120, 25);
		confirm.setEnabled(true);
		confirm.setActionCommand("confirm");
		confirm.addActionListener(this);
		confirm.setVisible(true);

		panel.add(confirm);

		accept = new JButton(Languages.getTranslation("accept"));
		accept.setSize(120, 25);
		accept.setEnabled(true);
		accept.setActionCommand("accept");
		accept.addActionListener(this);
		accept.setVisible(true);

		panel.add(accept);

		cancel = new JButton(Languages.getTranslation("cancel"));
		cancel.setSize(120, 25);
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
			this.gui.getMiddleware().getSettings().save();
			this.dispose();
		} else if ("cancel".equals(source)) {
			this.dispose();
		} else if ("accept".equals(source)) {
			this.setSettings();
			this.gui.getMiddleware().getSettings().save();
		}
	}
}
