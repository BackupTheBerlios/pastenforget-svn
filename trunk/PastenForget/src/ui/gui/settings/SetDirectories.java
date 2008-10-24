package ui.gui.settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import settings.Languages;
import ui.gui.GUI;
import ui.gui.dialog.Dialog;
import ui.gui.dialog.PathDialog;

public class SetDirectories extends JPanel implements SettingsInterface,
		ActionListener {

	private static final long serialVersionUID = 5852791272907519487L;

	private static final String LABEL = SettingsEnum.DIRECTORIES.getLabel();

	private GUI gui;

	private settings.Settings settings;
	
	private JPanel panel, panelOut;
	
	private JLabel label;
	
	private JButton browse;

	private JTextField downloadPath, srcPath;

	private File downloadDirectory = null;

	private File srcDirectory = null;
	
	private Dimension labelSize = Dialog.getLabelSizeMedium();

	private Dimension textFieldSize = Dialog.getTextFieldSizeMedium();

	private Dimension buttonSize = Dialog.getButtonSizeMedium();

	public SetDirectories(GUI gui) {
		this.gui = gui;
		settings = this.gui.getMiddleware().getSettings();
		this.setLayout(new BorderLayout());
		init();
	}
	
	private void init() {
		panelOut = new JPanel();
		panelOut.setLayout(new FlowLayout());
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(Languages
				.getTranslation("downloadfolder")));

		label = new JLabel(Languages.getTranslation("downloadfolder")
				+ ":");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);
		panel.add(label);

		downloadPath = new JTextField();
		if (settings.getDownloadDirectory() != null) {
			downloadDirectory = settings.getDownloadDirectory();
			downloadPath.setText(settings.getDownloadDirectory().toString());
		}
		downloadPath.setBackground(Color.WHITE);
		downloadPath.setSize(textFieldSize);
		downloadPath.setPreferredSize(textFieldSize);
		downloadPath.setVisible(true);
		panel.add(downloadPath);

		browse = new JButton(Languages.getTranslation("search"));
		browse.setSize(buttonSize);
		browse.setPreferredSize(buttonSize);
		browse.setEnabled(true);
		browse.setActionCommand("download");
		browse.addActionListener(this);
		browse.setVisible(true);
		panel.add(browse);

		panelOut.add(panel);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(Languages
				.getTranslation("sourcefolder")));

		label = new JLabel(Languages.getTranslation("sourcefolder") + ":");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);

		panel.add(label);

		srcPath = new JTextField();
		if (settings.getSrcDirectory() != null) {
			srcDirectory = settings.getSrcDirectory();
			srcPath.setText(settings.getSrcDirectory().toString());
		}
		srcPath.setBackground(Color.WHITE);
		srcPath.setSize(textFieldSize);
		srcPath.setPreferredSize(textFieldSize);
		srcPath.setVisible(true);
		panel.add(srcPath);

		browse = new JButton(Languages.getTranslation("search"));
		browse.setSize(buttonSize);
		browse.setPreferredSize(buttonSize);
		browse.setEnabled(true);
		browse.setActionCommand("src");
		browse.addActionListener(this);
		browse.setVisible(true);
		panel.add(browse);
		
		panelOut.add(panel);
		this.add(panelOut, BorderLayout.CENTER);
		
		label = new JLabel();
		label.setText(Languages.getTranslation("notice") + ": "
				+ Languages.getTranslation("noticefolders"));
		this.add(label, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	@Override
	public void accept() {
		settings.setDownloadDirectory(new File(downloadPath.getText()));
		settings.setSrcDirectory(new File(srcPath.getText()));
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
			downloadDirectory = new PathDialog(downloadPath.getText())
					.getDestination();
			if (downloadDirectory != null) {
				downloadPath.setText(downloadDirectory.getPath());
			}
		} else if ("src".equals(source)) {
			srcDirectory = new PathDialog(srcPath.getText()).getDestination();
			if (srcDirectory != null) {
				srcPath.setText(srcDirectory.getPath());
			}
		}

	}

}
