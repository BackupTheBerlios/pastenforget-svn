package captcha;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CaptchaDecrypter {
	protected final File file;
	protected List<Integer> rgbCode = new ArrayList<Integer>();
	protected BufferedImage img;
	
	public CaptchaDecrypter(String path) throws IOException {
		this.file = new File(path);
		this.prepareCaptcha();
	}
	
	private void prepareCaptcha() throws IOException {
		this.img = (BufferedImage)new ImageIcon(ImageIO.read(this.file)).getImage();
		for(int i = 0; i < this.img.getHeight(); i++) {
			for(int j = 0; j < this.img.getWidth(); j++ ) {
				this.rgbCode.add(this.img.getRGB(j, i));
			}
		}
	}
	
	public String decryptCaptcha() throws IOException, InterruptedException {
		return null;
	}
}
