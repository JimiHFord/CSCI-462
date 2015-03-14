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
		ARK3 ark = new ARK3( Hex.toByteArray(
			"00000000000000000000000000000000"
		));
//		Hex.
		String correct0 = ""
				+ "a5c6a5c6a5c6a5c6"
				+ "78269976e20f6e2d"
				+ "e9e024dc59dfb440"
				+ "84cd7da536399b7f"
				+ "e547a091852baf18"
				+ "22af8e832eab6334"
				+ "7b337ff541e743cd"
				+ "92b044d3d2a239e3"
				+ "dbaa039a460fcc08"
				+ "17dc4b912794cf7f"
				+ "acade1a5907cdab5"
				+ "5a2688981701a846"
				+ "6b7b3eba324b14df"
				+ "f5d6d3d82d572f84"
				+ "f320348d200ba25a"
				+ "106c78840bfc611a"
				+ "84832c71611e405c"
				+ "dc8c1a1ec4e4252d"
				+ "109a50ce83901ba3"
				+ "5be669d3b768b552"
				+ "6160685f4dc034db"
				+ "0e9ebbc7d637f1bf"
				+ "e3b7aea118199fcf"
				+ "ebd718f19f112a60"
				+ "244edb524690efbd"
				+ "b2c2578d4c03c72a"
				+ "e518024751556ecd";
		String correctf = ""
				+ "df443cefafef07ef"
				+ "4f0ceff659e8dd82"
				+ "bd755195e03e785e"
				+ "a2c798eb79699a4c"
				+ "466b033e672bc09c"
				+ "e9a47c63977104d5"
				+ "103dae86d3a47cdb"
				+ "52d0ea31d8ad98ec"
				+ "8a8cdcf3c690ec3c"
				+ "cc8eebf441e5c616"
				+ "6adfbb5e3223b540"
				+ "886de05885e6abbd"
				+ "ebe884370df1ae19"
				+ "1fb73788a9d72517"
				+ "3b07f9b441f9abce"
				+ "fe0649d538697d05"
				+ "436ef79ecf5b3a20"
				+ "3d26607659d1018d"
				+ "349ff93e00bafac5"
				+ "52ce5c8f454f99a9"
				+ "9516c336555777b2"
				+ "cc61646f86fb3086"
				+ "541702737c19a241"
				+ "fb324eb4162a3240"
				+ "2f65cf142951b6c1"
				+ "1897ee4c189d177d"
				+ "65882d6db258ffcd";
//		ARK3 ark = new ARK3( Hex.toByteArray(
//		"ffffffffffffffffffffffffffffffff"
//				));
		long plaintext = 0;
		long ciphertext = 0;
//		long ciphertext = 0x8114510682e7a4bf;
		long subkey = 0;
		ark.roundify(0);
		String temp,correct;
		for(int i = 1, s = 0; i <= NUMBER_OF_ROUNDS; i++, s+= 16) {
			temp = Hex.toString(ark.subkeys[i]);
			correct = correct0.substring(s, s+16);
//			correct = correctf.substring(s, s+16);
//			System.out.println(Hex.toString(ark.subkeys[i]));
			System.out.print(temp + '\t' + correct + '\t');
			if(!temp.equals(correct)) {
				System.out.print("***");
			}
			System.out.println();
		}
//		System.out.println(Hex.toString(ark.subkeys[0]));
	}
}
