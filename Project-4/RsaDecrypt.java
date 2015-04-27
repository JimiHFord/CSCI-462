//******************************************************************************
//
// File:    RsaDecrypt.java
// Package: ---
// Unit:    Class RsaDecrypt
//
//******************************************************************************

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
	 * main method performs the cracking algorithm on the given RSA data
	 * @param args command line arguments 
	 * <p>args[0] = &lt;file&gt;</p>
	 */
	public static void main (String[] args) {
		if(args.length != 1) {
			usage();
		}
		AList<RsaGroupInput> input = new AList<RsaGroupInput>();
		AList<String> output = new AList<String>();
		String publicModulus = null, publicExponent = null, ciphertext = null;
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(args[0]),
					Charset.forName("UTF-8"));
		} catch (IOException e) {
			error("There was an error reading the input file.\n"
					+ "Ensure that the file exists and that it contains at "
					+ "least 2 groups of 3 numeric values all on separate "
					+ "lines." + '\n'+
					'\t'+"1. public modulus" +'\n' +
					'\t'+"2. public exponent" + '\n'+
					'\t'+"3. ciphertext");
		}
		if(lines.size() %3 != 0) {
			error("Each \"group\" must consist of 3 numeric values, each on "
					+ "its own line."+'\n'+
					'\t'+"1. public modulus" +'\n' +
					'\t'+"2. public exponent" + '\n'+
					'\t'+"3. ciphertext");
		}
		if(lines.size() < 6) {
			error("File must contain at least 2 groups of values.");
		}
		try {
			for(int i = 0; i < lines.size(); i+=3) {
				publicModulus = lines.get(i + 0);
				publicExponent = lines.get(i + 1);
				ciphertext = lines.get(i + 2);
				input.addLast(new RsaGroupInput(
						publicModulus,
						publicExponent,
						ciphertext));
			}
		} catch (NumberFormatException e) {
			error("There was an error reading the input file.\n"
					+ "Ensure that the file exists and that it contains at "
					+ "least 2 groups of 3 numeric values all on separate "
					+ "lines." + '\n'+
					'\t'+"1. public modulus" +'\n' +
					'\t'+"2. public exponent" + '\n'+
					'\t'+"3. ciphertext");
		}	
		boolean found = false;
		RsaGroupInput alex, blake;
		BigInteger gcd;
		for(int i = 0; i < input.size(); i++) {
			alex = input.get(i);
			found = false;
			for(int j = 0; !found && j < input.size(); j++) { if(i != j) {
				blake = input.get(j);
				gcd = alex.publicMod.gcd(blake.publicMod);
				if(!gcd.equals(BigInteger.ONE) && 
						!alex.publicMod.equals(blake.publicMod)) {
					crack(alex, gcd, output);
					found = true;
				}
			}}
		}
		for(int i = 0; i < output.size(); i++) {
			System.out.println(output.get(i));
		}
	}
	
	/**
	 * Perform the necessary computations on the given information about
	 * cryptographic values in order to determine the plaintext and then
	 * store the plaintext in a list of strings.
	 * 
	 * @param alex data holder for a group of given information taken from
	 * 	the file
	 * @param gcd the gcd previously computed between the public moduli
	 * @param out the list to store the plaintext into
	 */
	private static void crack(RsaGroupInput alex, BigInteger gcd, 
			AList<String> out) {
		BigInteger alexPrime = alex.publicMod.divide(gcd);
		BigInteger alexPrivate = RSA.private_key(alex.pubExp, 
				alexPrime, gcd);
		out.addLast(convertBigInt(
				RSA.decrypt(alex.ciphertext, alexPrivate, 
						alex.publicMod)
				));
	}
	
	/**
	 * Convert a big int into a readable string using the procedure described
	 * in the project write-up.
	 * @param convert the big int to convert
	 * @return the readable string contained inside the integer
	 */
	private static String convertBigInt(BigInteger convert) {
		byte[] bytes = convert.toByteArray();
		int length = bytes[1] & 0xff;
		byte[] s = new byte[length];
		System.arraycopy(bytes, 2, s, 0, length );
		try {
			return new String(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new String(s);
		}
	}
	
	/**
	 * print usage message and exit
	 */
	private static void usage() {
		System.err.println("usage: java RsaDecrypt <file>");
		System.exit(1);
	}
	
	/**
	 * print error message and then call the usage method
	 * @param msg the error message to display
	 */
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
}
