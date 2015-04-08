public class PtCt48 {

	private final static long MASK_48 = 0xffffffffffffL;

	/**
	 * plaintext
	 */
	public final long pt;

	/**
	 * ciphertext
	 */
	public final long ct;

	public PtCt48(long pt, long ct) {
		this.pt = pt & MASK_48;
		this.ct = ct & MASK_48;
	}
}
