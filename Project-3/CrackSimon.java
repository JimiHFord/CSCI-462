//******************************************************************************
//
// File:    CrackSimon.java
// Package: ---
// Unit:    Class CrackSimon
//
//******************************************************************************

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.rit.util.AList;
import edu.rit.util.Hex;

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
	 * Figure out the 3 subkeys used to encrypt the plaintext supplied on the
	 * command line
	 * 
	 * @param args
	 *            the plaintext-ciphertext pairs
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			error("Must supply at least one plaintext and its associated "
					+ "ciphertext");
		}
		if (args.length % 2 != 0) {
			error("Every supplied known plaintext must be followed by its "
					+ "ciphertext");
		}
		AList<PtCt48> pairs = new AList<PtCt48>();
		long pt, ct;
		String a1, a2;
		Matcher a1m, a2m;
		Pattern p = Pattern.compile("^[0-9a-fA-F]{12}");
		for(int i = 0; i < args.length; i+=2) {
			a1 = args[i];
			a2 = args[i+1];
			a1m = p.matcher(a1);
			a2m = p.matcher(a2);
			if(!(a1m.matches() && a2m.matches())) {
				error("Each plaintext-ciphertext pair must be 12 " +
						"hexadecimal digits long.");
			}
			pt = Hex.toLong(a1);
			ct = Hex.toLong(a2);
			pairs.addLast(new PtCt48(pt, ct));
		}
		// loop through subkey 1 guesses
		int subkey2 = 0, subkey3 = 0, subkey1;
		int correctSubkey1 = 0;
		long round1Out = 0, round3In = 0, round2NoSubkey = 0;
		int r2L, r3L;
		int numPairs = pairs.size();
		PtCt48 current;
		boolean subkey1Working = true;
		boolean allPairsWorked = false;
		boolean initializingSubkeys;
		for(subkey1 = 0; subkey1 <= 0xffffff && !allPairsWorked; 
				subkey1++) {
			initializingSubkeys = true;
			subkey1Working = true;
			for(int pair = 0; pair < numPairs && subkey1Working; pair++) {
				current = pairs.get(pair);
				if(initializingSubkeys) {
					round1Out = Simon.forwardRound(current.pt, subkey1);
					round2NoSubkey = Simon.f(round1Out, Simon.FORWARD);
					subkey2 = current.ctL ^ Mask.chopHigh(round2NoSubkey);
					round3In = Simon.f(current.ct, Simon.BACKWARD);
					r2L = Mask.chopLow(round2NoSubkey);
					r3L = Mask.chopLow(round3In);
					subkey3 = r2L ^ r3L;
					initializingSubkeys = false;
				} else {
					subkey1Working = 
						Simon.round(
						Simon.round(
						Simon.round(current.pt, subkey1, Simon.FORWARD), 
						subkey2, Simon.FORWARD), 
						subkey3, Simon.FORWARD) == current.ct;
				}
				if(subkey1Working && pair == numPairs - 1) {
					allPairsWorked = true;
					correctSubkey1 = subkey1;
				}
			}
		}
		if(allPairsWorked) {
			System.out.println(
				Hex.toString(correctSubkey1).
					substring(2, 8).toUpperCase() + '\t' + 
				Hex.toString(subkey2).
					substring(2, 8).toUpperCase() + '\t' + 
				Hex.toString(subkey3).
					substring(2, 8).toUpperCase()
			);
		}
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
	 * 
	 * @param msg
	 *            the error message to print
	 */
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
}
