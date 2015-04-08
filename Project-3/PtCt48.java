public class PtCt48 {

	private final static long MASK_48 = Mask.MASK_48;
	private final static int MASK_24 = Mask.MASK_24;

	/**
	 * plaintext
	 */
	public final long pt;
	
	/**
	 * Lower 24 bits of plaintext
	 */
	public final int ptL;
	
	/**
	 * Upper 24 bits of plaintext
	 */
	public final int ptH;

	/**
	 * ciphertext
	 */
	public final long ct;
	
	/**
	 * Lower 24 bits of ciphertext
	 */
	public final int ctL;
	
	/**
	 * Upper 24 bits of ciphertext
	 */
	public final int ctH;

	public PtCt48(long pt, long ct) {
		this.pt = pt & MASK_48;
		this.ct = ct & MASK_48;
		ptL = Mask.chopLow(pt);
		ptH = Mask.chopHigh(pt);
		ctL = Mask.chopLow(ct);
		ctH = Mask.chopHigh(ct);
	}
}
