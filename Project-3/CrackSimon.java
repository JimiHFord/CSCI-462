//******************************************************************************
//
// File:    CrackSimon.java
// Package: ---
// Unit:    Class CrackSimon
//
//******************************************************************************


/**
 * This class is the main program that takes in command line arguments and 
 * attempts to recover an unknown key using a known plaintext attack on the 
 * Simon Block Cipher.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-2-2015
 */
public class CrackSimon {

	/**
	 * run the main method
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		if(args.length < 2) {
			error("Must supply at least one plaintext and its associated "
					+ "ciphertext");
		}
		if(args.length %2 != 0) {
			error("Every supplied known plaintext must be followed by its "
					+ "ciphertext");
		}
		System.out.println(args.length);
	}
	
	/**
	 * print usage message and exit
	 */
	private static void usage() {
		System.err.println("java CrackSimon <pt1> <ct1> [<pt2> <ct2> ...]");
		System.exit(1);
	}

	/**
	 * print an error message and call the usage() method
	 * @param msg the error message to print
	 */
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
}
