//******************************************************************************
//
// File:    Decrypt.java
// Package: (default)
// Unit:    Class Decrypt
//
//******************************************************************************

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 * 
 * @author  Jimi Ford
 * @version 2-18-15
 */
public class Decrypt {

	private final int KEY_LENGTH = KeyHelper.KEY_LENGTH;
	private String cipherText;
	private int[] key;
	
	// Error checking is done in main
	// prevent default construction
	private Decrypt() {
		
	}
	
	private Decrypt(int[] key, String cipherText) {
		this.key = key;
		this.cipherText = cipherText;
	}

	public String plainText() throws DecryptionException {
		StringBuilder builder = new StringBuilder();
		KeystreamGenerator gen = new KeystreamGenerator(key);
		int P, C, C_X;
		for(int i = 0; i < cipherText.length(); i++) {
			C = cipherText.charAt(i) - 'A';
			if(C < KEY_LENGTH && C >= 0) {
				C_X = C - gen.next();
				C_X = C_X < 0 ? C_X + KEY_LENGTH : C_X;
				P = (C_X) % KEY_LENGTH;
				builder.append((char)(P + 'A'));
			} else {
				throw new DecryptionException();
			}
		}
		return builder.toString();
	}
	
	public static void main(String[] args) {
		if(args.length != 3) {
			usage();
		}
		KeyHelper helper = new KeyHelper();
		if(!helper.isValidKey(args[0])) {
			displayError("The <key> must only consist of 26 unique letters of the english alphabet.");
		}
		int[] key = helper.initKey(args[0]);
		String cipherText = null;
		try {
			cipherText = new String(Files.readAllBytes(Paths.get(args[1])));
		} catch (IOException | InvalidPathException | OutOfMemoryError e) {
			displayError("The <ctfile> is either too large (over 2GB), or it was not found.");
		}
		Decrypt decryptor = new Decrypt(key,cipherText);
		String plainText = null;
		try {
			plainText = decryptor.plainText();
		} catch (DecryptionException e) {
			displayError("Error decrypting ciphertext file: " + args[1]);
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(args[2]);
			writer.print(plainText);
		} catch (FileNotFoundException | SecurityException e) {
			displayError("Error writing to \"" + args[2]+"\".");
		} finally {
			if(writer != null) writer.close();
		}
	}
	
	private static void usage() {
		System.err.println("java Decrypt <key> <ctfile> <ptfile>");
		System.exit(1);
	}
	
	private static void displayError(String msg) {
		System.err.println(msg);
		usage();
	}
	
	@SuppressWarnings("serial")
	private class DecryptionException extends Exception {
		
	}
}

