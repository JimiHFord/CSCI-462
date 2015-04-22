//******************************************************************************
//
// File:    RsaGroupInput.java
// Package: ---
// Unit:    Class RsaGroupInput
//
//******************************************************************************

import java.math.BigInteger;

/**
 * This class acts as a simple data container for the different values
 * in the input file. It exposes the BigInteger equivalent of the string
 * values found in the input file.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-22-2015
 */
public class RsaGroupInput {
	/**
	 * A 2048-bit RSA public modulus.
	 */
	public final BigInteger publicMod;
	
	/**
	 * An RSA public exponent.
	 */
	public final BigInteger pubExp;
	
	/**
	 * An RSA ciphertext.
	 */
	public final BigInteger ciphertext;
	
	/**
	 * construct an RsaGroupInput
	 * @param publicModulus public modulus
	 * @param publicExponent public exponent
	 * @param ciphertext corresponding ciphertext
	 */
	public RsaGroupInput(String publicModulus,
			String publicExponent, String ciphertext) {
		this.publicMod = new BigInteger(publicModulus);
		this.pubExp = new BigInteger(publicExponent);
		this.ciphertext = new BigInteger(ciphertext);
	}
}
