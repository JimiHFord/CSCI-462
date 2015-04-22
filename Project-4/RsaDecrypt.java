//******************************************************************************
//
// File:    RsaDecrypt.java
// Package: ---
// Unit:    Class RsaDecrypt
//
//******************************************************************************

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import edu.rit.util.AList;

/**
 * This class attacks RSA by taking at least 2 pairs of A 2048-bit RSA public 
 * modulus, an RSA public exponent, and an RSA ciphertext and computing the
 * plaintext from this information. The name of this attack is called a 
 * shared factor attack.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-22-2015
 *
 */
public class RsaDecrypt {

	/**
	 * main method
	 * @param args command line arguments 
	 * <p>args[0] = &lt;file&gt;</p>
	 */
	public static void main (String[] args) {
		if(args.length != 1) {
			usage();
		}
		AList<RsaGroupInput> input = new AList<RsaGroupInput>();
		AList<String> output = new AList<String>();
		RSA rsa = new RSA();
		int counter = 0, pairCount = 0;
		String publicModulus = null, publicExponent = null, ciphertext = null;
		try(BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        // process the line.
		    	switch(counter) {
		    	case 0:
		    		publicModulus = line;
		    		break;
		    	case 1:
		    		publicExponent = line;
		    		break;
		    	default:
		    		ciphertext = line;
		    		input.addLast(new RsaGroupInput(
		    				publicModulus, 
		    				publicExponent,
		    				ciphertext));
		    		pairCount++;
		    	}
		    	counter = (counter+1)%3;
		    }
		    // line is not visible here.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(counter!=0) {
			error("Each \"group\" must consist of 3 numeric values, each on "
					+ "its own line."+'\n'+
					'\t'+"1. public modulus" +'\n' +
					'\t'+"2. public exponent" + '\n'+
					'\t'+"3. ciphertext");
		}
		if(pairCount < 2) {
			error("File must contain at least 2 groups of values.");
		}
		for(int i = 0; i < input.size(); i++) {
			RsaGroupInput x = input.get(i);
		}
		RsaGroupInput alex = input.get(0);
		RsaGroupInput blake = input.get(1);
		final BigInteger gcd = alex.publicMod.gcd(blake.publicMod);
		for(int i = 0; i < input.size(); i++) {
			blake = input.get(i);
			crack(blake, gcd, output);
		}
		for(int i = 0; i < output.size(); i++) {
			System.out.println(output.get(i));
		}
	}
	
	private static void crack(RsaGroupInput blake, BigInteger gcd, 
			AList<String> output) {
		BigInteger blake_prime = blake.publicMod.divide(gcd);
		BigInteger blake_private = RSA.private_key(blake.pubExp, 
				blake_prime, gcd);
		output.addLast(convertBigInt(
				RSA.decrypt(blake.ciphertext, blake_private, 
						blake.publicMod)
				));
	}
	
	private static String convertBigInt(BigInteger convert) {
		byte[] bytes = convert.toByteArray();
		int length = bytes[1] & 0xff;
		byte[] s = new byte[length];
		System.arraycopy(bytes, 2, s, 0, length );
		return new String(s);
	}
	
	/**
	 * print usage message and exit
	 */
	private static void usage() {
		System.err.println("usage: java RsaDecrypt <file>");
		System.exit(1);
	}
	
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
}
