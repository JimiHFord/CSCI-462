//******************************************************************************
//
// File:    ARK3Permute.java
// Package: ---
// Unit:    Class ARK3Permute
//
//******************************************************************************

/**
 * Contains the specific ARK3 permution implementation. The permution function
 * takes in an array of bytes and mixes them up to increase the randomness of
 * ARK3's output. The ARK3 specifies that the bytes be permuted in the following
 * way:
 *
 * <P><TT>byte[0] -> byte[1]</TT></P>
 * <P><TT>byte[1] -> byte[4]</TT></P>
 * <P><TT>byte[2] -> byte[7]</TT></P>
 * <P><TT>byte[3] -> byte[2]</TT></P>
 * <P><TT>byte[4] -> byte[5]</TT></P>
 * <P><TT>byte[5] -> byte[0]</TT></P>
 * <P><TT>byte[6] -> byte[3]</TT></P>
 * <P><TT>byte[7] -> byte[6]</TT></P>
 * 
 * @author Jimi Ford (jhf3617)
 * @version 3-21-2015
 */
public class ARK3Permute {

	/**
	 * Permute the bytes according to the ARK3 standard
	 * 
	 * @param bytes the bytes to permute. Assumed to be length 8.
	 */
	public static void permute(byte[] bytes) {
		byte temp = bytes[5];
		bytes[5] = bytes[4];
		bytes[4] = bytes[1];
		bytes[1] = bytes[0];
		bytes[0] = temp;
		
		temp = bytes[3];
		bytes[3] = bytes[6];
		bytes[6] = bytes[7];
		bytes[7] = bytes[2];
		bytes[2] = temp;
	}
}
