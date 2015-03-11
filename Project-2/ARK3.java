import edu.rit.util.Hex;
import edu.rit.util.Packing;


public class ARK3 {

	public static final int NUMBER_OF_ROUNDS = 27;
	public static final int KEY_ROTATION = 29;
	
	private final byte[] key;
	// QUESTION: 2 64 bit longs for the key
	// bad idea / design?
	private long kH, kL;
	public byte[][] subkeys = new byte[NUMBER_OF_ROUNDS+1][Byte.SIZE];
	private byte[] key128 = new byte[16];
	private int round;
	
	public ARK3(byte[] key) {
		this.key = key.clone();
		kH = Packing.packLongBigEndian(key, 0);
		kL = Packing.packLongBigEndian(key, 8);
//		round = NUMBER_OF_ROUNDS;
		for(int i = 1; i <= NUMBER_OF_ROUNDS; i++) {
			rotateKey(KEY_ROTATION);
			/*
			 * Rotate the 128-bit key state 29 bits to the right.
				Divide the state into sixteen 8-bit bytes.
				Feed each of the eight most significant bytes through a different substitution box (S-box) as defined above.
				Permute the eight most significant bytes into a different order as defined above.
				Mix each pair of the eight most significant bytes together by a mix function as defined above.
				Add the round number (1 for the first round, 2 for the second round, etc.) to the least significant byte, using addition in the same field GF(28)/f(x) as the S-box.
				Rejoin the bytes into the 128-bit key state.
				The 64 most significant bits of the key state are the subkey.
			 */
			unpackKeyState();
			initSubkey(i);
			subPermuteMix(subkeys[i]);
			prepKeyState(i, subkeys[i]);
		}
	}
	
	private void prepKeyState(int i, byte[] bytes) {
		kH = Packing.packLongBigEndian(bytes, 0); // 0 for most significant
		int least = (int) (kL & 0xff);
		least = (GF28.add(least, i) & 0xff);
		kL = (kL & 0xffffffffffffff00L) | least;
	}

	public String keyStateString() {
		return Hex.toString(kH) + Hex.toString(kL);
	}
	
	private void unpackKeyState() {
		Packing.unpackLongBigEndian(kH, key128, 0);
		Packing.unpackLongBigEndian(kL, key128, 8);
	}
	
	private void initSubkey(int i) {
		Packing.unpackLongBigEndian(kH, subkeys[i], 0);
	}
	
	private void rotateKey(int amt) {
		if(amt % Long.SIZE == 0) return;
		long amtL = Long.SIZE - amt;
		long hiShiftedL = kH << amtL;
		long hiShiftedR = kH >>> amt;
		long loShiftedL = kL << amtL;
		long loShiftedR = kL >>> amt;
		kL = loShiftedR | hiShiftedL;
		kH = loShiftedL | hiShiftedR;
	}
	
	private void subPermuteMix(byte[] bytes) {
		SBox.box(bytes);
		SBox.permute(bytes);
		Mix.mix(bytes);
	}
	
	public byte[] encrypt(byte[] input) {
		return null;
	}
	
	public long encrypt(long input) {
		
		return 0;
	}
	
	public static void main(String[] args) {
		ARK3 ark = new ARK3(
				"00000000000000000000000000000000".getBytes());
		long plaintext = 0;
		long ciphertext = 0;
//		long ciphertext = 0x8114510682e7a4bf;
		long subkey = 0;
		for(int i = 1; i <= NUMBER_OF_ROUNDS; i++) {
			System.out.println(Hex.toString(ark.subkeys[i]));
		}
		System.out.println(Hex.toString(ark.subkeys[0]));
	}
}
