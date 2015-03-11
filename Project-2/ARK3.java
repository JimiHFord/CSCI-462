import edu.rit.util.Hex;
import edu.rit.util.Packing;


public class ARK3 {

	public static final int NUMBER_OF_ROUNDS = 27;
	public static final int KEY_ROTATION = 29;
	
	private final byte[] key, nonce;
	private long kH, kL;
	private int round;
	
	public ARK3(byte[] key, byte[] nonce) {
		this.key = key.clone();
		this.nonce = nonce.clone();
		kH = Packing.packLongBigEndian(key, 0);
		kL = Packing.packLongBigEndian(key, 8);
		round = NUMBER_OF_ROUNDS;
	}
	
	public String keyStateString() {
		return Hex.toString(kH) + Hex.toString(kL);
	}
	
	private void rotr(int amt) {
		if(amt % Long.SIZE == 0) return;
		long amtL = Long.SIZE - amt;
		long hiShiftedL = kH << amtL;
		long hiShiftedR = kH >>> amt;
		long loShiftedL = kL << amtL;
		long loShiftedR = kL >>> amt;
		kL = loShiftedR | hiShiftedL;
		kH = loShiftedL | hiShiftedR;
	}
	
	private int round() {
		if(++round > NUMBER_OF_ROUNDS) {
			round = 1;
		}
		return round;
	}
	
	public long round(long input) {
		long subkey = subkey();
		System.out.print(Hex.toString(subkey) + '\t');
		input ^= subkey;
		input = subAndMix(input);
		return input;
	}
	
	private long subAndMix(long input) {
		return Mix.mix(
				SBox.permute(
						SBox.box(input)));
	}
	
	public long subkey() {
		rotr(KEY_ROTATION);
		kH = subAndMix(kH);
		int add =  (int) (kL & 0xff);
		add = (GF28.add(add, round()) & 0xff);
		kL = (kL & 0xffffffffffffff00L) | add;
		return kH;
	}
	
	public byte[] encrypt(byte[] input) {
		return null;
	}
	
	public long encrypt(long input) {
		
		return 0;
	}
	
	public static void main(String[] args) {
		ARK3 ark = new ARK3(
				"00000000000000000000000000000000".getBytes(), 
				"0000000000000000".getBytes());
		long plaintext = 0;
		long ciphertext = 0;
//		long ciphertext = 0x8114510682e7a4bf;
		long subkey = 0;
		for(int i = 0; i < NUMBER_OF_ROUNDS; i++) {
//			subkey = ark.subkey();
//			System.out.println(Hex.toString(subkey));
			ciphertext = ark.round(ciphertext);
			System.out.println(Hex.toString(ciphertext));
		}
		
	}
}
