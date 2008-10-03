package captcha;


import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MegauploadDecrypter extends CaptchaDecrypter{
	
	public MegauploadDecrypter(String path) throws IOException {
		super(path);
	}
	
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
		differentColors.remove(0);
		
		for(int i = 0; i < img.getHeight(); i++) {
			for(int j = 0; j < img.getWidth(); j++ ) {
				int current = img.getRGB(j, i);
				if((current != differentColors.get(0).getRGB())&&(current != differentColors.get(1).getRGB())&&(current != differentColors.get(2).getRGB())) {
					img.setRGB(j, i, new Color(255, 255, 255).getRGB());
				}
			}
		}
		
		//ImageIO.write(img,"gif",new File("C:\\MegauploadCaptcha4.gif"));
		String captcha = new OCR().recognizeCharacters(img);
		
		return captcha;
		
	}
}
