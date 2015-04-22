import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.rit.util.AList;

//******************************************************************************
//
// File:    RsaDecrypt.java
// Package: ---
// Unit:    Class RsaDecrypt
//
//******************************************************************************

/**
 * This class attacks RSA by taking at least 2 pairs of A 2048-bit RSA public 
 * modulus, an RSA public exponent, and an RSA ciphertext and computing the
 * plaintext from this information.
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
		int count = 0;
		String publicModulus = null, publicExponent = null, ciphertext = null;
		try(BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        // process the line.
		    	switch(count) {
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
		    	}
		    	count = (count+1)%3;
		    }
		    // line is not visible here.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(count!=0) {
			error("Each \"group\" must consist of 3 values, each on "
					+ "its own line."+'\n'+
					'\t'+"1. public modulus" +'\n' +
					'\t'+"2. public exponent" + '\n'+
					'\t'+"3. ciphertext");
		}
		for(int i = 0; i < input.size(); i++) {
			RsaGroupInput x = input.get(i);
			System.out.println(x.publicModulus);
			System.out.println(x.publicExponent);
			System.out.println(x.ciphertext);
		}
	}
	
	private static void usage() {
		System.err.println("usage: java RsaDecrypt <file>");
		System.exit(1);
	}
	
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
}
