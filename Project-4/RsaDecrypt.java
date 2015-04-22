//******************************************************************************
//
// File:    RsaDecrypt.java
// Package: ---
// Unit:    Class RsaDecrypt
//
//******************************************************************************

/**
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-22-2015
 *
 */
public class RsaDecrypt {

	/**
	 * 
	 * @param args
	 */
	public static void main (String[] args) {
		if(args.length != 1) {
			usage();
		}
		
	}
	
	private static void usage() {
		System.err.println("usage: java RsaDecrypt <file>");
		System.exit(1);
	}
}
