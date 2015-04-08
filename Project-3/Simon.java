//******************************************************************************
//
// File:    Simon.java
// Package: ---
// Unit:    Class Simon
//
//******************************************************************************

import edu.rit.util.Hex;

/**
 * Class is the implementation of Simon block cipher. Its reusable methods that
 * don't depend on a set of 3 subkeys are broken up and made static as allow
 * the attacking program easy access to these methods. 
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-8-2015
 */
public class Simon {

	/**
	 * number of rounds in reduced round Simon
	 */
	public static final int NUM_ROUNDS = 3;

	/**
	 * size of the block used in Simon
	 */
	public static final int BLOCK_SIZE = 48;

	/**
	 * size of the block pieces after being split up
	 */
	public static final int PIECE_SIZE = 24;

	private static final int MASK_24 = Mask.MASK_24;
	private static final long MASK_48 = Mask.MASK_48;
	
	/**
	 * Direction for the round functions and f-function.
	 * Passing this constant into these functions will cause the method
	 * to run in the normal forward direction
	 */
	public static final boolean FORWARD = true;
	
	/**
	 * Direction for the round functions and f-function.
	 * Passing this constant into these functions will cause the method
	 * to run the inverse of the given method
	 */
	public static final boolean BACKWARD = false;

	// private data members
	private final int[] subkey;

	/**
	 * Construct an instance of Simon to encrypt or decrypt data
	 * 
	 * @param threeSubkeys the 3 subkeys to use for each round
	 */
	public Simon(int[] threeSubkeys) {
		this.subkey = threeSubkeys;
	}

	/**
	 * encrypt a 48-bit data value
	 * @param pt the 48-bit plaintext
	 * @return the encrypted plaintext (i.e. ciphertext)
	 */
	public long encrypt(long pt) {
		int[] arr = new int[2];
		fill(arr, pt);
		round(arr, subkey[0], true);
		round(arr, subkey[1], true);
		round(arr, subkey[2], true);
		return pack(arr);
	}

	/**
	 * decrypt a 48-bit data value
	 * @param ct the 48-bit ciphertext
	 * @return the decrypted ciphertext (i.e. plaintext)
	 */
	public long decrypt(long ct) {
		int[] arr = new int[2];
		fill(arr, ct);
		round(arr, subkey[2], false);
		round(arr, subkey[1], false);
		round(arr, subkey[0], false);
		return pack(arr);
	}

	/**
	 * fill an array with the top and bottom halves of a given long
	 * @param arr the array to populate with the 2 sets of 24 bits
	 * 		arr[0] will contain the MSBs of <TT>l</TT>
	 * 		arr[1] will contain the LSBs of <TT>l</TT>
	 * @param l the given long to split up into 2 24-bit chunks
	 */
	private static void fill(int[] arr, long l) {
		arr[0] = (int) ((l >>> PIECE_SIZE) & MASK_24);
		arr[1] = (int) (l & MASK_24);
	}

	/**
	 * pack the contents of an array into a single 48-bit value
	 * @param arr array containing the 2 24-bit values. 
	 * 		Assumed to be length == 2
	 * @return the packed 48-bit value
	 */
	private static long pack(int[] arr) {
		return ((((long) arr[0]) << PIECE_SIZE) | ((long) arr[1])) & MASK_48;
	}

	/**
	 * Compute the feistel network function without using a subkey
	 * @param arr 
	 * 		arr[0] = the left MSB state bits
	 * 		arr[1] = the right MSB state bits 
	 * @param forward use true to compute the forward function, and false to 
	 * compute the inverse function
	 */
	public static void f(int[] arr, boolean forward) {
		if(forward) {
			int rotl1, rotl2, rotl8;
			int left = arr[0], right = arr[1];
			rotl1 = rotl24bit(left, 1);
			rotl2 = rotl24bit(left, 2);
			rotl8 = rotl24bit(left, 8);
			left = (((rotl1 & rotl8) ^ right) ^ rotl2);
			arr[1] = arr[0];
			arr[0] = left;
		} else {
			int rotr1, rotr2, rotr8, swap;
			swap = arr[0];
			arr[0] = arr[1];
			arr[1] = swap;
			int left = arr[0], right = arr[1];
			rotr1 = rotl24bit(left, 1);
			rotr2 = rotl24bit(left, 2);
			rotr8 = rotl24bit(left, 8);
			right ^= rotr2;
			right ^= (rotr1 & rotr8);
			arr[1] = right;
		}
	}
	
	/**
	 * Compute the feistel network function without using a subkey
	 * 
	 * @param in the 48-bit state 
	 * @param forward use true to compute the forward function, and false to 
	 * compute the inverse function
	 * @return the 
	 */
	public static long f(long in, boolean forward) {
		int[] arr = new int[2];
		fill(arr, in);
		f(arr, forward);
		return pack(arr);
	}
	
	/**
	 * compute the full round function of Simon using a specified subkey
	 * 
	 * @param in the 48-bit state
	 * @param subkey the subkey to use for this round
	 * @param forward the direction of the round (true for forward, 
	 * 		false for inverse)
	 * @return the 48-bit state after executing this round
	 */
	public static long round(long in, int subkey, boolean forward) {
		int[] arr = new int[2];
		fill(arr, in);
		round(arr, subkey, forward);
		return pack(arr);
	}
	
	/**
	 * Compute the full round function of Simon using a specified subkey
	 * 
	 * @param arr array containing the 2 24-bit states
	 * @param subkey the subkey to use for this round
	 * @param forward the direction of the round (true for forward, false for
	 * 		inverse)
	 */
	public static void round(int[] arr, int subkey, boolean forward) {
		f(arr, forward);
		arr[forward ? 0 : 1] ^= subkey;
	}
	
	/**
	 * Compute the forward round function with a specified subkey
	 * 
	 * @param in the 48-bit state
	 * @param subkey the subkey to use for this round
	 * @return the 48-bit state after execution of this round
	 */
	public static long forwardRound(long in, int subkey) {
		return round(in, subkey, true);
	}
	
	/**
	 * Compute the inverse round function with a specified subkey
	 * 
	 * @param in the 48-bit state
	 * @param subkey the subkey to use for this round
	 * @return the 48-bit state after execution of this round
	 */
	public static long backwardsRound(long in, int subkey) {
		return round(in, subkey, false);
	}

	/**
	 * rotate a 24-bit value a certain amount to the left
	 * @param src the value to rotate
	 * @param amt the amount to rotate by
	 * @return the rotated integer
	 */
	private static int rotl24bit(int src, int amt) {
		return rot24bit(src, amt, false);
	}

	/**
	 * rotate a 24-bit value a certain amount either to the left or right
	 * @param src the value to rotate
	 * @param amt the amount to rotate by
	 * @param right true if rotating to the right, false if rotating to the left
	 * @return the rotated integer
	 */
	private static int rot24bit(int src, int amt, boolean right) {
		int lAmt = right ? PIECE_SIZE - amt : amt;
		int rAmt = right ? amt : PIECE_SIZE - amt;
		int sL = src << lAmt;
		int sR = src >>> rAmt;
		return (sL | sR) & MASK_24;
	}

	// 16D64E9F444F 7E799E 0F65B9 8DEAB8
	// output: 00001107AB88C1BB
	// 178F860127E4 D602B2 4B7662 FDA03A
	// output: 0000D9AC5948864A
	// C7117352274B C69593 177EB2 4EE6FD
	// output: 0000CCD320E2B803
	// 96169406B093 8E01CE 9BDF55 E63DB9
	// output: 000085DFC8F77BEA
	/**
	 * Unit test program for Simon
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 4) {
			usage();
		}
		int[] subkeys = { Hex.toInt(args[1]), Hex.toInt(args[2]),
				Hex.toInt(args[3]) };
		long pt = Hex.toLong(args[0]);
		System.out.println(Hex.toString(pt).toUpperCase().substring(4, 16));
		Simon simon = new Simon(subkeys);
		long ct = simon.encrypt(pt);
		pt = simon.decrypt(ct);
		System.out.println(Hex.toString(ct).toUpperCase().substring(4, 16));
		System.out.println(Hex.toString(pt).toUpperCase().substring(4, 16));
	}

	/**
	 * print a usage message and exit
	 */
	private static void usage() {
		System.err
				.println("usage: java Simon <pt> <sk1> <sk2> <sk3>");
		System.exit(1);
	}
}
