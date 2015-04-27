//******************************************************************************
//
// File:    RSA.java
// Package: ---
// Unit:    Class RSA
//
//******************************************************************************

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Class contains the necessary methods used in the RSA standard of public
 * key encryption.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-22-2015
 */
public class RSA {

	/**
	 * Encrypt a string with RSA public key encryption
	 * @param plaintext the string to encrypt
	 * @param e the private exponent used to encrypt the string
	 * @param n the public modulus used
	 * @return the corresponding ciphertext
	 */
	public static BigInteger encrypt(String plaintext, 
			BigInteger e, BigInteger n) {

		String s = plaintext;
		byte[] sbytes = new byte[] { };
		try {
			sbytes = s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {	}
		byte[] ptbytes = new byte[256];
		ptbytes[0] = 1;
		ptbytes[1] = (byte) sbytes.length;
		System.arraycopy(sbytes, 0, ptbytes, 2, sbytes.length);
		BigInteger m = new BigInteger(1, ptbytes);
		BigInteger c = m.modPow(e, n);
		return c;
	}

	/**
	 * perform the decryption operation used in RSA
	 * @param ciphertext the ciphertext to decrypt into plaintext
	 * @param exponent the private exponent used for the decryption
	 * @param mod the public modulus used for encryption
	 * @return the corresponding plaintext 
	 */
	public static BigInteger decrypt(
			BigInteger ciphertext, BigInteger exponent, BigInteger mod) {
		return ciphertext.modPow(exponent, mod);
	}

	/**
	 * deduce the private key from the given information and the computed info
	 * 
	 * @param pub_exp the known public exponent
	 * @param p one of the 2 primes used to encrypt the plaintext
	 * @param q the other prime used to encrypt the plaintext
	 * @return the private key exponent that was used to encrypt the plaintext
	 */
	public static BigInteger private_key(BigInteger pub_exp, 
			BigInteger p, BigInteger q) {
		BigInteger phi_n = phi_n(p, q);
		return pub_exp.modPow(BigInteger.valueOf(-1), phi_n);
	}

	/**
	 * compute phi of n given two primes
	 * 
	 * @param p one of the RSA primes
	 * @param q the other RSA prime used
	 * @return phi of n = (p-1)*(q-1)
	 */
	public static BigInteger phi_n(BigInteger p, BigInteger q) {
		return p.subtract(BigInteger.ONE).
				multiply(q.subtract(BigInteger.ONE));
	}
}
