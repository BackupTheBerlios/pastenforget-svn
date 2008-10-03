package ui.gui.dialog;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InfoDialog extends JDialog {

	private static final long serialVersionUID = 7462916120115541801L;

	Container c;

	public InfoDialog() {
		c = this.getContentPane();
		this.setLocation(new Point(150, 150));
		this.setPreferredSize(new Dimension(400, 400));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public void help() {
		c.setLayout(new GridLayout(2, 1, 10, 10));
		c.add(new JLabel("...", JLabel.CENTER));
		c.add(new JLabel("...", JLabel.CENTER));
		this.setTitle("Hilfe");
		this.setVisible(true);
		this.pack();
	}

	public void info() {
		c.setLayout(new FlowLayout());

		JLabel label = new JLabel(new ImageIcon("images/banner.png"));
		c.add(label);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(360, 300));

		JTextArea info = new JTextArea();
		info.setEditable(false);
		info.setPreferredSize(new Dimension(350, 290));
		info.setLayout(new FlowLayout(FlowLayout.LEFT));

		Dimension dimension = new Dimension(320, 20);
		label = new JLabel("Paste 'n' Forget 0.2 (Alpha)");
		label.setFont(new Font("font", Font.BOLD, label.getFont().getSize()));
		label.setPreferredSize(dimension);
		info.add(label);

		JTextArea area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setSize(dimension.width, 50);
		area.setFont(new Font("test", Font.PLAIN, label.getFont().getSize()));
		area
				.setText("Paste 'n' Forget ist ein Downloader für sogenannte Filehoster. Es vereinfacht den Download für Free-User bei Rapidshare, Megaupload, Uploaded und Netload.");
		info.add(area);

		label = new JLabel("Programmierer: Undertaker, Executor");
		label.setPreferredSize(dimension);
		info.add(label);

		label = new JLabel("Oberfläche: Executor");
		label.setPreferredSize(dimension);
		info.add(label);

		label = new JLabel("Grafikdesign: art_DELiRiUM");
		label.setPreferredSize(dimension);
		info.add(label);

		info.setVisible(true);
		scrollPane.setViewportView(info);

		c.add(scrollPane);
		this.setTitle("Info");
		this.setVisible(true);
		this.pack();
	}
}
