package core.hoster;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AwtImage extends JFrame{
	private BufferedImage img;

	public AwtImage(BufferedImage img) {
		this.img = img;
		this.init();
		this.setVisible(true);
	}
	
	public void init() {
		int height = this.img.getHeight();
		int width = this.img.getWidth();
		
		
		JPanel imagePanel = new JPanel() {
			Image backImage = img;

			public void paintComponent(Graphics g) {
				Graphics innerG = g.create();
				if(backImage == null) {
					innerG.setColor(new Color(0xffccccff));
					innerG.fill3DRect(0, 0, getSize().width, getSize().height, true);
				}
				else {
					innerG.drawImage(backImage, 0, 0, getSize().width, getSize().height, this);
				}

			}
		};
		
		
		imagePanel.setBounds(0, 0, width, height);
		this.getContentPane().setLayout(null);
		this.setSize(220, 100);
		this.add(imagePanel);
		JButton button = new JButton("�bernehmen");
		JTextField textField = new JTextField();
		button.setBounds(70, height, 120, 20);
		textField.setBounds(10, height, 50, 20);
		this.add(button);
		this.add(textField);
	}
}
