package ui.gui.settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.gui.GUI;
import ui.gui.dialog.PathDialog;

public class SetDirectories extends JPanel implements SettingsInterface,
		ActionListener {

	private static final long serialVersionUID = 5852791272907519487L;

	private static final String LABEL = "Ordner";

	private GUI gui;

	private settings.Settings settings;

	private JTextField downloadPath, ddlPath;

	private File downloadDirectory = null;

	private File ddlDirectory = null;

	public SetDirectories(GUI gui) {
		this.gui = gui;
		settings = this.gui.getMiddleware().getSettings();
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(540, 40));
		
		JLabel label = new JLabel("Downloadordner:");
		label.setPreferredSize(new Dimension(140, 25));
		label.setVisible(true);

		panel.add(label);

		downloadPath = new JTextField();
		if (settings.getDownloadDirectory() != null) {
			downloadDirectory = settings.getDownloadDirectory();
			downloadPath.setText(settings.getDownloadDirectory().toString());
		}
		downloadPath.setBackground(Color.WHITE);
		downloadPath.setSize(300, 25);
		downloadPath.setPreferredSize(new Dimension(300, 25));
		downloadPath.setVisible(true);

		panel.add(downloadPath);

		JButton browse = new JButton("Suchen");
		browse.setSize(120, 25);
		browse.setEnabled(true);
		browse.setActionCommand("download");
		browse.addActionListener(this);
		browse.setVisible(true);

		panel.add(browse);
		
		this.add(panel,BorderLayout.NORTH);
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(540, 40));

		label = new JLabel("DDL-Warez Ordner:");
		label.setPreferredSize(new Dimension(140, 25));
		label.setVisible(true);

		panel.add(label);

		ddlPath = new JTextField();
		if (settings.getDdlDirectory() != null) {
			ddlDirectory = settings.getDdlDirectory();
			ddlPath.setText(settings.getDdlDirectory().toString());
		}
		ddlPath.setBackground(Color.WHITE);
		ddlPath.setSize(300, 25);
		ddlPath.setPreferredSize(new Dimension(300, 25));
		ddlPath.setVisible(true);

		panel.add(ddlPath);

		browse = new JButton("Suchen");
		browse.setSize(120, 25);
		browse.setEnabled(true);
		browse.setActionCommand("ddl");
		browse.addActionListener(this);
		browse.setVisible(true);

		panel.add(browse);
		
		this.add(panel, BorderLayout.CENTER);
		this.setVisible(true);
	}

	@Override
	public void accept() {
		settings.setDownloadDirectory(downloadDirectory);
		settings.setDdlDirectory(ddlDirectory);
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("download".equals(source)) {
			downloadDirectory = new PathDialog().getDestination();
			if (downloadDirectory != null) {
				downloadPath.setText(downloadDirectory.getPath());
			}
		} else if ("ddl".equals(source)) {
			ddlDirectory = new PathDialog().getDestination();
			if (ddlDirectory != null) {
				ddlPath.setText(ddlDirectory.getPath());
			}
		}

	}

}
