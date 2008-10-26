package ui.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import middleware.Tools;
import settings.Languages;
import ui.gui.GUI;
import download.Download;

public class CaptchaDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -7459402167878262668L;

	private JPanel panel;

	private JLabel label;

	private JTextField textField;

	private JButton confirm, cancel, renew;
	
	private Download download;

	private Dimension windowSize = Dialog.getWindowsSizeCaptcha();

	private Dimension labelSize = Dialog.getLabelSizeSmall();
	
	private Dimension labelSize2 = Dialog.getLabelSizeBig();

	private Dimension textFieldSize = Dialog.getTextFieldSizeSmall();

	private Dimension buttonSize = Dialog.getButtonSizeMedium();

	Container c;

	public CaptchaDialog(GUI gui, Download download) {
		super(gui, "Captcha");
		this.download = download;

		this.setResizable(false);
		this.setSize(windowSize);
		this.setPreferredSize(windowSize);
		this.setLocation(Tools.getCenteredLocation(windowSize));
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		c = this.getContentPane();
		c.setLayout(new BorderLayout());

		init();
	}

	private void init() {
		panel = new JPanel();

		label = new JLabel(Languages.getTranslation("filename") + " :");
		label.setSize(labelSize);
		label.setPreferredSize(labelSize);
		label.setVisible(true);
		panel.add(label);
		
		label = new JLabel(download.getFileName());
		label.setSize(labelSize2);
		label.setPreferredSize(labelSize2);
		label.setVisible(true);
		panel.add(label);
		
		panel.setVisible(true);
		this.add(panel, BorderLayout.NORTH);

		panel = new JPanel();

		panel.setBorder(new TitledBorder("Captcha"));
		label = new JLabel(new ImageIcon(download.getCaptcha()));
		panel.add(label);
		
		textField = new JTextField();
		textField.setBackground(Color.WHITE);
		textField.setSize(textFieldSize);
		textField.setPreferredSize(textFieldSize);
		textField.setVisible(true);
		panel.add(textField);

		panel.setVisible(true);
		this.add(panel, BorderLayout.CENTER);
		
		panel = new JPanel();

		confirm = new JButton(Languages.getTranslation("confirm"));
		confirm.setSize(buttonSize);
		confirm.setPreferredSize(buttonSize);
		confirm.setEnabled(true);
		confirm.setActionCommand("confirm");
		confirm.addActionListener(this);
		confirm.setVisible(true);
		panel.add(confirm);

		renew = new JButton(Languages.getTranslation("renew"));
		renew.setSize(buttonSize);
		renew.setPreferredSize(buttonSize);
		renew.setEnabled(true);
		renew.setActionCommand("renew");
		renew.addActionListener(this);
		renew.setVisible(true);
		panel.add(renew);
		
		cancel = new JButton(Languages.getTranslation("cancel"));
		cancel.setSize(buttonSize);
		cancel.setPreferredSize(buttonSize);
		cancel.setEnabled(true);
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		cancel.setVisible(true);
		panel.add(cancel);

		panel.setVisible(true);
		this.add(panel, BorderLayout.SOUTH);

		this.pack();
		this.setVisible(true);
	}
	
	private void exit() {
		download.setCaptchaCode("cancel");
		this.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("cancel".equals(source)) {
			exit();
		} else if ("confirm".equals(source)) {
			if (textField.getText() != null && !"".equals(textField.getText())) {
				download.setCaptchaCode(textField.getText());
				this.dispose();
			} else {
				download.setCaptchaCode("new");
				this.dispose();
			}
		} else if ("renew".equals(source)) {
			download.setCaptchaCode("new");
			this.dispose();
		}

	}
	
}
