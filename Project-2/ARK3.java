import edu.rit.util.Hex;
import edu.rit.util.Packing;


public class ARK3 {

	private final byte[] key, nonce;
	private long kH, kL;
	
	public ARK3(byte[] key, byte[] nonce) {
		this.key = key.clone();
		this.nonce = nonce.clone();
		kH = Packing.packLongBigEndian(key, 0);
		kL = Packing.packLongBigEndian(key, 8);
	}
	
	public String keyStateString() {
		return Hex.toString(kH) + Hex.toString(kL);
	}
	
	public void rotr(int amt) {
		if(amt % Long.SIZE == 0) return;
		long amtL = Long.SIZE - amt;
		long hiShiftedL = kH << amtL;
		long hiShiftedR = kH >>> amt;
		long loShiftedL = kL << amtL;
		long loShiftedR = kL >>> amt;
		kL = loShiftedR | hiShiftedL;
		kH = loShiftedL | hiShiftedR;
	}
	
	public long encrypt(long input) {
		
		return 0;
	}
}
