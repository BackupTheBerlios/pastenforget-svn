package decrypt;

public class Word {
	private final int[] word;

	public Word(int byte1, int byte2, int byte3, int byte4) {
		this.word = new int[] { byte1, byte2, byte3, byte4 };
	}

	public void shiftLeft() {
		int highestByte = this.word[0];
		for (int i = 0; i < 3; i++) {
			this.word[i] = this.word[i + 1];
		}
		this.word[3] = highestByte;
	}

	public int getByte(int index) {
		return this.word[index];
	}
	
	public void setByte(int index, int byteValue) {
		this.word[index] = byteValue;
	}
}
