//******************************************************************************
//
// File:    Sanitize.java
// Package: ---
// Unit:    Class Sanitize
//
//******************************************************************************

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.rit.util.Hex;

/**
 * Class contains methods necessary to ensure proper command line arguments.
 * Sanitize also contains information about the proper length of the inputs
 * to use with Encrypt and Decrypt.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 3-21-2015
 */
public class Sanitize {

	/**
	 * How many characters long the key should be.
	 */
	public static final int KEY_STRING_LENGTH = 32;
	
	/**
	 * How many characters long the initialization vector should be
	 */
	public static final int IV_STRING_LENGTH = 16;
	
	/**
	 * Validate the key used in the command line
	 * @param key the given key to sanitize
	 * @param argName the name of the parameter being sanitized
	 * @return the sanitized key
	 * @throws SanitizationException if they key does not pass validation
	 */
	public static byte[] sanitizeKey(String key, String argName) 
		throws SanitizationException {
		Pattern p = Pattern.compile("^[0-9a-fA-F]{" + KEY_STRING_LENGTH+"}");
		Matcher m = p.matcher(key);
		if(!m.matches())
			throw new SanitizationException(
				String.format("The %1s must be %d hexadecimal characters.",
						argName, KEY_STRING_LENGTH));
		
		return Hex.toByteArray(key);
	}
	
	/**
	 * Validate the initialization vector used in the command line
	 * @param nonce the given initialization vector
	 * @param argName the name of the parameter being sanitized
	 * @return the sanitized initialization vector (or number used once)
	 * @throws SanitizationException if the initialization vector does not
	 * pass validation
	 */
	public static byte[] sanitizeNonce(String nonce, String argName)
		throws SanitizationException {
		Pattern p = Pattern.compile("^[0-9a-fA-F]{" + IV_STRING_LENGTH+"}");
		Matcher m = p.matcher(nonce);
		if(!m.matches())
			throw new SanitizationException(
				String.format("The %1s must be %d hexadecimal characters.",
						argName, IV_STRING_LENGTH));
		return Hex.toByteArray(nonce);
	}
}
