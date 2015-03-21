//******************************************************************************
//
// File:    ARK3Mix.java
// Package: ---
// Unit:    Class ARK3Mix
//
//******************************************************************************

/**
 * ARK3Mix contains methods necessary to implement the mix operation for
 * ARK3 block cipher.
 * 
 * @author Jimi Ford (jhf3617)
 *
 */
public class ARK3Mix {

	/**
	 * Perform ARK3 mix operation on bytes
	 * @param bytes the byte array to mix (assumed to be length 8)
	 */
	public static void mix(byte[] bytes) {
		mix(0, bytes);
		mix(1, bytes);
		mix(2, bytes);
		mix(3, bytes);
	}
	
	/**
	 * Perform the mix on a specific pair of of bytes
	 * @param pair the pair of bytes. Pair 0 will mix 0 and 1, pair 1 will mix
	 * 		2 and 3, pair 2 will mix 4 and 5, pair 3 will mix 6 and 7
	 * @param bytes the byte array to mix, only <TT>pair<TT>*2 and 
	 * 		<TT>pair<TT>*2 + 1 will be modified.
	 */
	private static void mix(int pair, byte[] bytes) {
		byte a, b, c, d;
		int p0 = pair*2;
		int p1 = p0 + 1;
		a = bytes[p0];
		b = bytes[p1];
		c = (byte) (0xff & GF28.add(
				GF28.multiply(0b00000010, a & 0xff, 
						ARK3.IRREDUCIBLE),b & 0xff));
		
		d = (byte) (0xff & GF28.add(a & 0xff, 
				GF28.multiply(0b00000011, b & 0xff, ARK3.IRREDUCIBLE)));
		bytes[p0] = c;
		bytes[p1] = d;
	}
}
