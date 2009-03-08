package ui.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import download.DownloadTools;

import middleware.Tools;
import settings.Languages;
import settings.Settings;
import ui.gui.GUI;

/**
 * Dialog für eine Suchanfrage.
 * 
 * @author executor
 * 
 */
public class MultiDownloadDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -8357043899768903230L;

	private JTextArea textArea;

	private Dimension windowSize = Dialog.getWindowsSizeIrcSearch();

	private Dimension labelSize = Dialog.getLabelSizeMedium();

	private Dimension buttonSize = Dialog.getButtonSizeMedium();

	Container c;

	public MultiDownloadDialog(GUI gui) {
		super(gui);
		this.setTitle(Languages.getTranslation("search"));
		this.setResizable(true);
		this.setSize(windowSize);
		this.setPreferredSize(windowSize);
		this.setMinimumSize(windowSize);
		this.setLocation(Tools.getCenteredLocation(windowSize));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		c = this.getContentPane();
		c.setLayout(new BorderLayout());

		init();
	}

	private void init() {
		JLabel label = new JLabel(Languages.getTranslation("multidownload") + ":");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);
		this.add(label, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		textArea = new JTextArea("...");
		scrollPane.add(textArea);
		scrollPane.setViewportView(textArea);
		this.add(scrollPane, BorderLayout.CENTER);

		JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton button = new JButton(Languages.getTranslation("download"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("download");
		button.addActionListener(this);
		button.setVisible(true);
		panelButton.add(button);

		button = new JButton(Languages.getTranslation("cancel"));
		button.setSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setEnabled(true);
		button.setActionCommand("cancel");
		button.addActionListener(this);
		button.setVisible(true);
		panelButton.add(button);
		
		this.add(panelButton, BorderLayout.SOUTH);

		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("download".equals(source)) {
			URL url = null;
			String []textLines = this.textArea.getText().split("\n");
			for(String line:textLines) {
				try {
					url = new URL(line);
					DownloadTools.addDownload(url, Settings.getDownloadDirectory());
				} catch (MalformedURLException e1) {
					System.out.println("MultiDownloadDialog: wrong URL format");
				}
			}
			this.dispose();
		} else if ("cancel".equals(source)) {
			this.dispose();
		}
	}

}
