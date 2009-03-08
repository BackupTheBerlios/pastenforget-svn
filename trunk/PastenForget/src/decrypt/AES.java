package decrypt;

public class AES {
	private final int[][] key;
	private int[][] roundKey;
	private final int[][] plaintext;
	private int[][] state;
	private final int keyLength;
	private final int numberOfRounds;
	private final int blockSize;

	private final int[][] fixedMatrix = new int[][] {
			{ 0x02, 0x03, 0x01, 0x01 },
			{ 0x01, 0x02, 0x03, 0x01 },
			{ 0x01, 0x01, 0x02, 0x03 },
			{ 0x03, 0x01, 0x01, 0x02 }
	};
	
	private String[] hexValue = new String[] {
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"
	};
	                                       
	
	
	public AES(int[][] plaintext, int[][] key) {
		this.key = key;
		this.plaintext = plaintext;
		this.keyLength = key.length;
		this.numberOfRounds = keyLength + 6;
		this.blockSize = 4;
		this.copyPlaintext();
		this.copyKey();
		System.out.println("Key Length: " + keyLength);
		System.out.println("Block Size: " + blockSize);
		System.out.println("Number of Rounds: " + numberOfRounds);
	}
	
	private void copyPlaintext() {
		this.state = new int[this.plaintext.length][this.plaintext[0].length];
		for(int i = 0; i < this.plaintext.length; i++) {
			for(int j = 0; j < this.plaintext[i].length; j++) {
				this.state[i][j] = this.plaintext[i][j];
			}
		}
	}
	
	private void copyKey() {
		this.roundKey = new int[this.key.length][this.key[0].length];
		for(int i = 0; i < this.key.length; i++) {
			for(int j = 0; j < this.key[i].length; j++) {
				this.roundKey[i][j] = this.key[i][j];
			}
		}
	}
	
	public void subBytes() {
		for(int i = 0; i < this.blockSize; i++) {
			for(int j = 0; j < this.blockSize; j++) {
				int substituted = SubBox.getValue(this.state[i][j]);
				this.state[i][j] = substituted;
			}
		}
	}
	
	public void displayState() {
		System.out.println("Current State Array:");
		for(int i = 0; i < this.state.length; i++) {
			System.out.print("[");
			for(int j = 0; j < this.state[i].length; j++) {
				String x = this.hexValue[this.state[i][j] >> 4];
				String y = this.hexValue[this.state[i][j] & 0x0F];
				System.out.print((j < 3) ? x + y + ", " : x + y);
			}
			System.out.println("]");
		}
		System.out.println("");
	}
	
	public void displayRoundKey() {
		System.out.println("Current Round Key:");
		for(int i = 0; i < this.roundKey.length; i++) {
			System.out.print("[");
			for(int j = 0; j < this.roundKey[i].length; j++) {
				String x = this.hexValue[this.roundKey[i][j] >> 4];
				String y = this.hexValue[this.roundKey[i][j] & 0x0F];
				System.out.print((j < 3) ? x + y + ", " : x + y);
			}
			System.out.println("]");
		}
		System.out.println("");
	}
	
	
	
	public void mixColumns() {
		int[][] newState = new int[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				int sum = 0;
				for(int k = 0; k < 4; k++) {
					sum ^= this.multiply(this.fixedMatrix[i][k], this.state[k][j]);
				}
				newState[i][j] = sum;
			}
		}
		this.state = newState;
	}
	
	
	private int multiply(int factorA, int factorB) {
		int product = 0;
		int highBitSet = 0;
		for(int i = 0; i < 8; i++) {
			if((factorB & 0x01) == 1) {
				product ^= factorA;
			}
			highBitSet = (factorA & 0x80);
			factorA <<= 1;
			factorA &= 0x0FF;
			if(highBitSet == 0x80) {
				factorA ^= 0x1B;
			}
			factorB >>= 1;
		}
		return product;
	}
	
	
	
	
	
	private void shiftRows() {
		for(int i = 0; i < 4; i++) {
			int[] rowCopy = new int[] {
				this.state[i][0],
				this.state[i][1],
				this.state[i][2],
				this.state[i][3]
			};
			for(int j = 0; j < 4; j++) {
				this.state[i][j] = rowCopy[(j + i) % 4]; 	
			}
			
		}
	}

	/*
	private void generateRoundKey() {
		int byte0 = this.roundKey[0][3];
		for(int i = 0; i < 4; i++) {
			if(i == 3) {
				this.roundKey[3][3] = byte0;
			} else {
				this.roundKey[i][3] = this.roundKey[i+1][3];
			}
			
			int substituted = SubBox.getValue(this.roundKey[i][3]);
			this.roundKey[i][3] = (i == 0) ? (substituted - 1) : substituted;
			this.roundKey[i][3] ^= this.roundKey[i][0]; 
		}
	}
	*/
	
	private void addRoundKey() {
		for(int i = 0; i < this.blockSize; i++) {
			for(int j = 0; j < this.blockSize; j++) {
				this.state[i][j] = this.state[i][j] ^ this.key[i][j];
				
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		int[][] plaintext = { 
				{0x54, 0x4F, 0x4E, 0x20},
				{0x77, 0x6E, 0x69, 0x54},
				{0x6F, 0x65, 0x6E, 0x77}, 
				{0x20, 0x20, 0x65, 0x6F}
		};
		
		int[][] key = {
				{0x54, 0x73, 0x20, 0x67},
				{0x68, 0x20, 0x4B, 0x20},
				{0x61, 0x6D, 0x75, 0x46},
				{0x74, 0x79, 0x6E, 0x75}
		};
		AES aes = new AES(plaintext, key);
		aes.addRoundKey();
		aes.displayState();
		aes.subBytes();
		aes.displayState();
		aes.shiftRows();
		aes.displayState();
		aes.mixColumns();
		aes.displayState();
	}
}
