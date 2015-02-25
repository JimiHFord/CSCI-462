//******************************************************************************
//
// File:    Encrypt.java
// Package: (default)
// Unit:    Class Encrypt
//
//******************************************************************************

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 * Class encrypt is responsible for encrypting a file using Letter-RC4. The
 * key for this program is passed into the runtime command line arguments and
 * should only consist of 26 unique english letters (lowercase or uppercase) in
 * any permutation. You must encrypt the plaintext file with the same key you 
 * intend to use in decrypting the ciphertext file.
 * 
 * @author  Jimi Ford
 * @version 2-18-15
 */
public class Encrypt {

	// private data members
	private final int KEY_LENGTH = KeyHelper.KEY_LENGTH;
	private String plainText;
	private int[] key;
	
	/**
	 * Construct an Encrypt object. It is assumed error checking is done
	 * in {@link #main(String[])} hence the private constructor.
	 * 
	 * @param key must consist of only 26 unique numbers ranging from 0 to 25
	 * 		  and should only be 26 indices long.
	 * @param plainText the plaintext to encrypt
	 */
	private Encrypt(int[] key, String plainText) {
		this.key = key;
		this.plainText = plainText.toUpperCase();
	}
	
	/**
	 * Perform the encryption of the plaintext. Characters that don't fall
	 * within A-Z or a-z are thrown away (or discarded). Only english letters
	 * are encrypted. The encryption always generates uppercase ciphertext.
	 * 
	 * @return the ciphertext for the given plaintext
	 */
	public String cipherText() {
		StringBuilder builder = new StringBuilder();
		KeystreamGenerator gen = new KeystreamGenerator(key);
		int P, C;
		for(int i = 0; i < plainText.length(); i++) {
			P = plainText.charAt(i) - 'A';
			if(P < KEY_LENGTH && P >= 0) {
				C = (P + gen.next()) % KEY_LENGTH;
				builder.append((char)(C + 'A'));
			}
		}
		return builder.toString();
	}
	
	/**
	 * The main method for this program. This method should be launched from
	 * the command line.
	 * 
	 * @param args should consist of only the &lt;key&gt; at index 0, 
	 * 			&lt;ptfile&gt; at index 1, and &lt;ctfile&gt; at index 2.
	 */
	public static void main(String[] args) {
		if(args.length != 3) {
			usage();
		}
		KeyHelper helper = new KeyHelper();
		if(!helper.isValidKey(args[0])) {
			displayError("The <key> must only consist of 26 unique letters of the english alphabet.");
		}
		int[] key = helper.initKey(args[0]);
		String plainText = null;
		try {
			plainText = new String(Files.readAllBytes(Paths.get(args[1])));
		} catch (IOException | InvalidPathException e) {
			displayError("The <ctfile> is either too large, or it was not found.");
		}
		Encrypt encryptor = new Encrypt(key, plainText);
		String cipherText = encryptor.cipherText();
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(args[2]);
			writer.print(cipherText);
			writer.close();
		} catch (FileNotFoundException e) {
			displayError("Error writing to \"" + args[2] + "\".");
		}
	}
	
	/**
	 * Print usage message for program and gracefully exit.
	 */
	private static void usage() {
		System.err.println("usage: java Encrypt <key> <ptfile> <ctfile>");
		System.exit(1);
	}

	/**
	 * Display an error message, then call the {@link #usage()} method.
	 * @param msg the error message to display
	 */
	private static void displayError(String msg) {
		System.err.println(msg);
		usage();
	}
}
