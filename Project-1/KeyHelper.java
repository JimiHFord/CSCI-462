//******************************************************************************
//
// File:    KeyHelper.java
// Package: (default)
// Unit:    Class KeyHelper
//
//******************************************************************************

import java.util.regex.Pattern;

/**
 * Class KeyHelper contains all the necessary information about the key used
 * for encrypting and decrypting with Letter-RC4, as well as the necessary
 * helper methods to validate and initialize a key.
 * 
 * @author  Jimi Ford
 * @version 2-18-15
 */
public class KeyHelper {

	/**
	 * The length of the key used for Letter-RC4
	 */
	public static final int KEY_LENGTH = 26;
	
	/**
	 * Check if the given key is a valid key. A key is valid if and only if
	 * it contains all 26 letters of the English alphabet, and it is 
	 *  exactly 26 characters long.
	 * @param 	key The key to validate.
	 * @return 	true if it matches the above criteria, otherwise false.
	 */
	public boolean isValidKey(String key) {
		key = key.toUpperCase();
		Pattern usagePattern = Pattern.compile("[A-Z]*");
		if(!usagePattern.matcher(key).matches()) {
			return false;
		}
		if(key.length() != KEY_LENGTH) {
			return false;
		}
		boolean[] flag = new boolean[KEY_LENGTH];
		for(int i = 0; i < key.length(); i++) {
			int c = key.charAt(i) - 'A';
			if(!flag[c]) {
				flag[c] = true;
			} else {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Initialize the key into an array of integers. Integer values are
	 * much easier to work with (when programming crypto algorithms)
	 * than characters are because of the ASCII bias or offset.
	 * @param 	key The key to initialize and convert to an array of
	 * 			integers.
	 * @return	An array of integer values ranging from 0 to 25. "A" 
	 * 			(or "a") becomes 0 and "Z" (or "z") becomes 25.
	 */
	public int[] initKey(String key) {
		key = key.toUpperCase();
		int[] retval = new int[KEY_LENGTH];
		if(isValidKey(key)) {
			for(int i = 0; i < key.length(); i++) {
				retval[i] = key.charAt(i) - 'A';
			}
		}
		return retval;
	}
}
