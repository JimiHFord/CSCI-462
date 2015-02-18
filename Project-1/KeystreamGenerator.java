//******************************************************************************
//
// File:    KeystreamGenerator.java
// Package: (default)
// Unit:    Class KeystreamGenerator
//
//******************************************************************************

/**
 * Class KeystreamGenerator acts as the keystream for Letter-RC4. It is not
 * reusable, meaning that 2 separate pieces of ciphertext and plaintext must 
 * use 2 separate instances of this class even if they share the same key
 * even if they share the same key.
 * 
 * @author  Jimi Ford
 * @version 2-18-15
 */
public class KeystreamGenerator {

	// Hidden data members
	private int i = 0;
	private int j = 0;
	private final int KEY_LENGTH = KeyHelper.KEY_LENGTH;
	private final int[] key;
	
	// prevent default construction without a key
	@SuppressWarnings("unused")
	private KeystreamGenerator() {
		key = null;
	}
	
	/**
	 * Construct a KeystreamGenerator with the given key in its initial state.
	 * @param 	key The key size must be exactly 26 and contain every number from
	 * 			0 to 25 inclusive.
	 */
	public KeystreamGenerator(int[] key) {
		this.key = key.clone();
	}

	/**
	 * Get the next pseudorandom integer.
	 * @return a pseudorandom integer ranging from 0 to 25 inclusive.
	 */
	public int next() {
		i = (i + 1) % KEY_LENGTH;
		j = (j + key[i]) % KEY_LENGTH;
		int temp = key[i];
		key[i] = key[j];
		key[j] = temp;
		return key[(key[i] + key[j]) % KEY_LENGTH];
	}
}
