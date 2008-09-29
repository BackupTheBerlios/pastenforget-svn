package captcha;

public class RGBPair implements Comparable<RGBPair>{
	private int rgb;
	private int count = 0;
	
	public RGBPair(int rgb) {
		this.rgb = rgb;
	}
	
	public int getRGB() {
		return this.rgb;
	}
	
	public int getCounter() {
		return this.count;
	}
	
	public void count() {
		this.count++;
	}
	
	public int compareTo(RGBPair rgb) {
		return rgb.getCounter() - this.count;
	}
	
}
