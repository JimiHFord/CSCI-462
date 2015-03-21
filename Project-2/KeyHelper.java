//******************************************************************************
//
// File:    KeyHelper.java
// Package: ---
// Unit:    Class KeyHelper
//
//******************************************************************************

/**
 * This class contains the method that combines the byte array
 * key and the byte array nonce into a single byte array. It is
 * shared with Encrypt and Decrypt to conform to "DRY" coding.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 3-21-2015
 */
public class KeyHelper {

	/**
	 * Combine the byte array key and the byte array initialization
	 * vector into a single byte array.
	 * 
	 * @param key the ARK3 key (assumed to be length 16)
	 * @param nonce the ARK3CFB initialization vector (assumed to be
	 * 			length 8)
	 * @return the single array containing the key in the first
	 * 			16 positions (0-15) and the iv in the remaining 8
	 */
	public static byte[] combineARK3KeyAndNonce(byte[] key, byte[] nonce) {
		byte[] arkKey = new byte[key.length + nonce.length];
		System.arraycopy(key, 0, arkKey, 0, key.length);
		System.arraycopy(nonce, 0, arkKey, key.length, nonce.length);
		return arkKey;
	}
}
