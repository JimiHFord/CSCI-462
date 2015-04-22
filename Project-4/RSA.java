//******************************************************************************
//
// File:    RSA.java
// Package: ---
// Unit:    Class RSA
//
//******************************************************************************

import java.math.BigInteger;


public class RSA {
	
	public BigInteger encrypt(String plaintext, 
			BigInteger e, BigInteger n) {
		
		String s = plaintext;
		byte[] sbytes = s.getBytes();
		byte[] ptbytes = new byte[256];
		ptbytes[0] = 1;
		ptbytes[1] = (byte) sbytes.length;
		System.arraycopy(sbytes, 0, ptbytes, 2, sbytes.length + 1);
		BigInteger m = new BigInteger(1, ptbytes);
		BigInteger c = m.modPow(e, n);
		return c;
	}
	
	public static BigInteger decrypt(
			BigInteger ciphertext, BigInteger exponent, BigInteger mod) {
		return ciphertext.modPow(exponent, mod);
	}
	
	public static BigInteger private_key(BigInteger pub_exp, 
			BigInteger p1, BigInteger p2) {
		BigInteger phi_n = p1.subtract(BigInteger.ONE).
				multiply(p2.subtract(BigInteger.ONE));
		return pub_exp.modPow(BigInteger.valueOf(-1), phi_n);
	}
}
