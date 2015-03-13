import edu.rit.util.Hex;
import edu.rit.util.Packing;


public class ARK3 {

	public static final int NUMBER_OF_ROUNDS = 27;
	public static final int KEY_ROTATION = 29;
	public static final int BLOCK_SIZE = 64;
	
	private final byte[] key;
	// QUESTION: 2 64 bit longs for the key
	// bad idea / design?
	private long kH, kL;
	public byte[][] subkeys = new byte[NUMBER_OF_ROUNDS+1][8];
	private byte[] key128 = new byte[16];
	
	public ARK3(byte[] key) {
		this.key = key.clone();
//		System.out.println(Hex.toString(key));
		kH = Packing.packLongBigEndian(key, 0);
		kL = Packing.packLongBigEndian(key, 8);
		for(int i = 1; i <= NUMBER_OF_ROUNDS; i++) {
			rotateKey(KEY_ROTATION);
			unpackKeyState();
			initSubkey(i);
			subPermuteMix(subkeys[i]);
			prepKeyState(i, subkeys[i]);
		}
	}
	
	private void prepKeyState(int i, byte[] bytes) {
		kH = Packing.packLongBigEndian(bytes, 0); // 0 for most significant
//		kL = 
		int least = (int) (kL & 0xff);
		least = (GF28.add(least, i & 0xff) & 0xff);
		long leastL = least;
		kL = (kL & 0xffffffffffffff00L) | (leastL & 0xff);
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
	
	public void xor(byte[] a, byte[] b, byte[] dst) {
		
	}
	
	private void xor(byte[] src, byte[] dst) {
		if(src.length != dst.length) return;
		for(int i = 0; i < src.length; i++) {
			dst[i] ^= src[i];
		}
	}
	
	private byte[] roundify(byte[] bytes) {
		for(int i = 1; i <= NUMBER_OF_ROUNDS; i++) {
			xor(subkeys[i], bytes);
			subPermuteMix(bytes);
		}
		return bytes;
	}
	
	public long roundify(long input) {
		byte[] bytes = new byte[8];
		Packing.unpackLongBigEndian(input, bytes, 0);
		long output = Packing.packLongBigEndian(roundify(bytes), 0);
		return output;
	}
	
	public static void main(String[] args) {
//		ARK3 ark = new ARK3( Hex.toByteArray(
//			"00000000000000000000000000000000"
//		));
//		Hex.
		ARK3 ark = new ARK3( Hex.toByteArray(
		"ffffffffffffffffffffffffffffffff"
				));
		long plaintext = 0;
		long ciphertext = 0;
//		long ciphertext = 0x8114510682e7a4bf;
		long subkey = 0;
		ark.roundify(0);
//		for(int i = 1; i <= NUMBER_OF_ROUNDS; i++) {
//			System.out.println(Hex.toString(ark.subkeys[i]));
//		}
//		System.out.println(Hex.toString(ark.subkeys[0]));
	}
}
