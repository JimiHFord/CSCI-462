import edu.rit.util.Packing;


public class ARK3 {

	public static final int NUMBER_OF_ROUNDS = 27;
	public static final int KEY_ROTATION = 29;
	public static final int BLOCK_SIZE = 64;
	public static final int KEY_SIZE = 16;
	
	private byte[] key;
	private long kH, kL;
	public byte[][] subkeys = new byte[NUMBER_OF_ROUNDS+1][8];
	
	
	public void setKey(byte[] key) {
		this.key = new byte[KEY_SIZE];
		System.arraycopy(key, 0, this.key, 0, KEY_SIZE);
		kH = Packing.packLongBigEndian(this.key, 0);
		kL = Packing.packLongBigEndian(this.key, 8);
		for(int i = 1; i <= NUMBER_OF_ROUNDS; i++) {
			rotateKey(KEY_ROTATION);
			initSubkey(i);
			subPermuteMix(subkeys[i]);
			prepKeyState(i, subkeys[i]);
		}
	}
	
	private void prepKeyState(int i, byte[] bytes) {
		kH = Packing.packLongBigEndian(bytes, 0); // 0 for most significant
		int least = (int) (kL & 0xff);
		least = (GF28.add(least, i & 0xff) & 0xff);
		long leastL = least;
		kL = (kL & 0xffffffffffffff00L) | (leastL & 0xff);
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
	
	private void xor(byte[] src, byte[] dst) {
		if(src.length != dst.length) return;
		for(int i = 0; i < src.length; i++) {
			dst[i] ^= src[i];
		}
	}
	
	public byte[] encrypt(byte[] bytes) {
		for(int i = 1; i <= NUMBER_OF_ROUNDS; i++) {
			xor(subkeys[i], bytes);
			subPermuteMix(bytes);
		}
		return bytes;
	}
}
