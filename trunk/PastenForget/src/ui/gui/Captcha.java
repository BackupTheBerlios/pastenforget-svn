package ui.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Captcha extends JFrame {

	Container c;
	private Image image;
	private static final long serialVersionUID = -1848119122507353652L;
	private JComponent a;

	public Captcha(Image image) {
		this.image = image;
		c = getContentPane();
		c.setLayout(new BorderLayout());

		this.setSize(new Dimension(250, 100));
		this.setPreferredSize(new Dimension(250, 100));
		this.setMinimumSize(new Dimension(250, 100));

		JTextField captchaCode = new JTextField();
		JButton submit = new JButton("bestätigen");

		c.add(captchaCode, BorderLayout.CENTER);
		c.add(submit, BorderLayout.SOUTH);

		this.setTitle("Captcha Eingabe");
		this.setResizable(true);
		this.setLocation(new Point(100, 100));
		this.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		InputStream is = new URL(
				"http://tbn0.google.com/images?q=tbn:F2mRdK2oPs0UeM:http://www.sympatec.com/DE/LaserDiffraction/images/DiffractionSmall_300.jpg")
				.openConnection().getInputStream();
		Image image = ImageIO.read(is);
		new Captcha(image);
	}

}
