//******************************************************************************
//
// File:    PtCt48.java
// Package: ---
// Unit:    Class PtCt48
//
//******************************************************************************

/**
 * Class holds a 48-bit plaintext, 48-bit ciphertext pair.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-8-2015
 */
public class PtCt48 {

	// private data members
	
	private final static long MASK_48 = Mask.MASK_48;

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

	/**
	 * construct a plaintext-ciphertext pair
	 * @param pt the plaintext
	 * @param ct the plaintext's corresponding ciphertext
	 */
	public PtCt48(long pt, long ct) {
		this.pt = pt & MASK_48;
		this.ct = ct & MASK_48;
		ptL = Mask.chopLow(pt);
		ptH = Mask.chopHigh(pt);
		ctL = Mask.chopLow(ct);
		ctH = Mask.chopHigh(ct);
	}
}
