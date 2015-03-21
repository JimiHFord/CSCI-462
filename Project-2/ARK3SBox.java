//******************************************************************************
//
// File:    ARK3SBox.java
// Package: ---
// Unit:    Class ARK3SBox
//
//******************************************************************************

/**
 * Contains the methods necessary for implementation of the SBox specified in
 * ARK3. The SBox takes in an array of bytes and manipulates the bytes on a bit
 * level utilizing GF 2^8 mathematics.
 * 
 * @author Jimi Ford (jhf3617)
 *
 */
public class ARK3SBox {

	/**
	 * Irreducible polynomial used in ARK3
	 */
	private static final int IRREDUCIBLE = ARK3.IRREDUCIBLE;
	
	

	/**
	 * Run all 8 bytes through the 8 SBox's
	 * 
	 * @param bytes the bytes to run through the SBox. Assumed to be length 8.
	 */
	public static void box(byte[] bytes) {
		bytes[0] = box(1, bytes[0]);
		bytes[1] = box(2, bytes[1]);
		bytes[2] = box(3, bytes[2]);
		bytes[3] = box(4, bytes[3]);
		bytes[4] = box(5, bytes[4]);
		bytes[5] = box(6, bytes[5]);
		bytes[6] = box(7, bytes[6]);
		bytes[7] = box(8, bytes[7]);
	}
	
	/**
	 * Perform the SBox operation given which "box number" it is
	 * 
	 * @param i integer from 1-8 inclusive representing which SBox it is
	 * @param input the input byte into the SBox
	 * @return the result from the SBox sub-i operation
	 */
	private static byte box(int i, byte input) {
		int leftTerm = GF28.multiply(
				GF28.add(0b11000000, i & 0xff), 
				input & 0xff, IRREDUCIBLE) & 0xff;
		int rightTerm = 0b01100011;
		byte retval = (byte)(GF28.add(leftTerm, rightTerm) & 0xff); 
		return retval;
	}
}
