package core.captcha;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

public class MegauploadDecrypter extends CaptchaDecrypter {
	
	public MegauploadDecrypter(String path) throws IOException {
		super(path);
	}
	
	@Override
	public String decryptCaptcha() throws IOException, InterruptedException {
		List<RGBPair> differentColors = new ArrayList<RGBPair>();
		
		
		for(int i = 0; i < this.rgbCode.size(); i++) {
			Integer currentPixel = this.rgbCode.get(i); 
			boolean isInList = false;
			for(int j = 0; j < differentColors.size(); j++) {
				if(currentPixel.equals(differentColors.get(j).getRGB())) {
					differentColors.get(j).count();
					isInList = true;
				}
			}
			if(isInList == false) {
				RGBPair rgbPair = new RGBPair(currentPixel);
				rgbPair.count();
				differentColors.add(rgbPair);
			}
		}
		
		Collections.sort(differentColors);
		differentColors.remove(0); // häufigste Farbe: weiß
		
		for(int i = 0; i < differentColors.size(); i++) {
			System.out.println(differentColors.get(i).getRGB() + " : " + differentColors.get(i).getCounter());
		}
		
		
		for(int i = 0; i < img.getHeight(); i++) {
			for(int j = 0; j < img.getWidth(); j++ ) {
				if((img.getRGB(j, i) != differentColors.get(0).getRGB())&&(img.getRGB(j, i) != differentColors.get(1).getRGB())&&(img.getRGB(j, i) != differentColors.get(2).getRGB())) {
					img.setRGB(j, i, new Color(255, 255, 255).getRGB());
				}
			}
		}
		
		
		ImageIO.write(img,"gif",new File("C:\\MegauploadCaptcha4.gif"));
		
		/*
		 *  Imagefile dem OCR-Algorithmus übergeben
		 */
		//TODO
		return null;
		
	}
}
