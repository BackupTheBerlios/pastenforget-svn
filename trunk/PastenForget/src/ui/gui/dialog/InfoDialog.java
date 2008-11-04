package ui.gui.dialog;

import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import middleware.Tools;
import settings.Languages;
import ui.gui.GUI;

public class InfoDialog extends JDialog {

	private static final long serialVersionUID = 7462916120115541801L;

	private Dimension windowSize = new Dimension(380, 455);

	Container c;

	public InfoDialog(GUI gui) {
		super(gui, Languages.getTranslation("download"));

		this.setResizable(false);
		this.setSize(windowSize);
		this.setPreferredSize(windowSize);
		this.setLocation(Tools.getCenteredLocation(windowSize));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		c = this.getContentPane();
		c.setLayout(new FlowLayout());

		init();
	}

	public void init() {

		JLabel label = new JLabel(new ImageIcon(Tools.getProgramPath()
				.getAbsolutePath()
				+ "/images/banner.png"));
		c.add(label);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(360, 300));

		JTextArea info = new JTextArea();
		info.setEditable(false);
		info.setPreferredSize(new Dimension(350, 290));
		info.setLayout(new FlowLayout(FlowLayout.LEFT));

		Dimension dimension = new Dimension(320, 20);
		label = new JLabel("Paste 'n' Forget 0.400 (Alpha)");
		label.setFont(new Font("font", Font.BOLD, label.getFont().getSize()));
		label.setPreferredSize(dimension);
		info.add(label);

		JTextArea area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setSize(dimension.width, 50);
		area.setFont(new Font("test", Font.PLAIN, label.getFont().getSize()));
		area.setText(Languages.getTranslation("infopnf"));
		info.add(area);

		label = new JLabel(Languages.getTranslation("website")
				+ ": http://pastenforget.berlios.de");
		label.setPreferredSize(dimension);
		info.add(label);

		label = new JLabel(Languages.getTranslation("programmer")
				+ ": Undertaker, Executor");
		label.setPreferredSize(dimension);
		info.add(label);

		label = new JLabel(Languages.getTranslation("ui") + ": Executor");
		label.setPreferredSize(dimension);
		info.add(label);
		
		label = new JLabel("Graphic-Design: Julio van Dyke");
		label.setPreferredSize(dimension);
		info.add(label);

		label = new JLabel(Languages.getTranslation("tester") + ": Acid Green");
		label.setPreferredSize(dimension);
		info.add(label);

		label = new JLabel(Languages.getTranslation("licence")
				+ ": GNU General Public License v3");
		label.setPreferredSize(dimension);
		info.add(label);

		info.setVisible(true);
		scrollPane.setViewportView(info);

		c.add(scrollPane);

		JButton button = new JButton(Languages.getTranslation("confirm"));
		button.setPreferredSize(Dialog.getButtonSizeMedium());
		button.setVisible(true);
		button.setActionCommand("confirm");
		button.addActionListener(new ButtonListener(this));
		c.add(button);

		button = new JButton(Languages.getTranslation("website"));
		button.setPreferredSize(Dialog.getButtonSizeMedium());
		button.setVisible(true);
		button.setActionCommand("website");
		button.addActionListener(new ButtonListener(this));
		c.add(button);

		button = new JButton(Languages.getTranslation("licence"));
		button.setPreferredSize(Dialog.getButtonSizeMedium());
		button.setVisible(true);
		button.setActionCommand("licence");
		button.addActionListener(new ButtonListener(this));
		c.add(button);

		this.setTitle("Info");
		this.setVisible(true);
		this.pack();
	}

	private class ButtonListener implements ActionListener {

		private JDialog dialog;

		public ButtonListener(JDialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			if ("confirm".equals(ae.getActionCommand())) {
				this.dialog.dispose();
			} else if ("website".equals(ae.getActionCommand())) {
				try {
					Desktop.getDesktop().browse(
							new URI("http://pastenforget.berlios.de"));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			} else if ("licence".equals(ae.getActionCommand())) {
				try {
					Desktop.getDesktop().browse(
							new URI("http://www.gnu.org/licenses/gpl.html"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

}
