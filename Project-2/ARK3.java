//******************************************************************************
//
// File:    ARK3.java
// Package: ---
// Unit:    Class ARK3
//
//******************************************************************************

import edu.rit.util.Packing;

/**
 * ARK3 is a block cipher invented by Prof. Alan Kaminsky. Its security
 * is unknown and should therefore not be used in any real world scenario. This
 * class contains the operations specific to subkey generation and storage,
 * the encryption functionality, and keystate manipulations. It utilizes three
 * other classes, ARK3SBox, ARK3Mix, and ARK3Permute to perform specialized
 * reusable operations found in its project specification.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 3-21-2015
 */
public class ARK3 {

	/**
	 * Number of rounds used in ARK3
	 */
	public static final int NUMBER_OF_ROUNDS = 27;
	
	/**
	 * Number of bits to rotate by in each round
	 */
	public static final int KEY_ROTATION = 29;
	
	/**
	 * Number of bits in ARK3's block
	 */
	public static final int BLOCK_SIZE = 64;
	
	/**
	 * Size of ARK3's Key in bytes
	 */
	public static final int KEY_SIZE = 16;
	
	/**
	 * Irreducible GF 2^8 polynomial used for GF 2^8 mathematics.
	 * x^8+x^4+x^3+x^2+x^0
	 */
	public static final int IRREDUCIBLE =
			0b100011101;
	
	// private data members
	private byte[] key;
	private long kH, kL;
	private byte[][] subkeys = new byte[NUMBER_OF_ROUNDS+1][8];
	
	/**
	 * Set the key for ARK3. Assumed to be length 16
	 * @param key the key used for encryption
	 */
	public void setKey(byte[] key) {
		this.key = new byte[KEY_SIZE];
		System.arraycopy(key, 0, this.key, 0, KEY_SIZE);
		kH = Packing.packLongBigEndian(this.key, 0);
		kL = Packing.packLongBigEndian(this.key, 8);
		for(int i = 1; i <= NUMBER_OF_ROUNDS; i++) {
			rotateKey();
			initSubkey(subkeys[i]);
			subPermuteMix(subkeys[i]);
			prepKeyState(i, subkeys[i]);
		}
	}
	
	/**
	 * After the subkey has been substituted through the SBox, permuted and
	 * mixed, perform the last step in ARK3's round function. This handles 
	 * adding the round number to the least significant byte and storing the
	 * result in ARK3's keystate.
	 * @param i which round number this instance is at (1-27 inclusive)
	 * @param bytes the bytes of the current subkey
	 */
	private void prepKeyState(int i, byte[] bytes) {
		kH = Packing.packLongBigEndian(bytes, 0); // 0 for most significant
		int least = (int) (kL & 0xff);
		least = (GF28.add(least, i & 0xff) & 0xff);
		long leastL = least;
		kL = (kL & 0xffffffffffffff00L) | (leastL & 0xff);
	}
	
	/**
	 * Unpack the current keystate into the specified subkey
	 * @param i the index of the subkey to initialize (1-27 inclusive)
	 */
	private void initSubkey(byte[] bytes) {
		Packing.unpackLongBigEndian(kH, bytes, 0);
	}
	
	/**
	 * Perform the rotation by <TT>KEY_ROTATION</TT> amount where 
	 * <TT>KEY_ROTATION</TT> is the number of bits to rotate by.
	 * (29 for ARK3)
	 */
	private void rotateKey() {
		long amtL = Long.SIZE - KEY_ROTATION;
		long hiShiftedL = kH << amtL;
		long hiShiftedR = kH >>> KEY_ROTATION;
		long loShiftedL = kL << amtL;
		long loShiftedR = kL >>> KEY_ROTATION;
		kL = loShiftedR | hiShiftedL;
		kH = loShiftedL | hiShiftedR;
	}
	
	/**
	 * Perform the ARK3 Substitution Box operation, the ARK3 Permutation
	 * operation, and the ARK3 Mix function operation on the given byte array.
	 * @param bytes the byte array to perform these operations on
	 */
	private void subPermuteMix(byte[] bytes) {
		ARK3SBox.box(bytes);
		ARK3Permute.permute(bytes);
		ARK3Mix.mix(bytes);
	}
	
	/**
	 * X-OR the source byte array into the destination byte array
	 * @param src the source byte array (remains unchanged)
	 * @param dst the destination byte array containing the result of the
	 * 			xor operation
	 */
	private void xor(byte[] src, byte[] dst) {
		if(src.length != dst.length) return;
		for(int i = 0; i < src.length; i++) {
			dst[i] ^= src[i];
		}
	}
	
	/**
	 * Encrypt the given byte array
	 * @param bytes the byte array to encrypt
	 */
	public void encrypt(byte[] bytes) {
		for(int i = 1; i <= NUMBER_OF_ROUNDS; i++) {
			xor(subkeys[i], bytes);
			subPermuteMix(bytes);
		}
	}
}
