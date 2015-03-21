//******************************************************************************
//
// File:    Decrypt.java
// Package: ---
// Unit:    Class Decrypt
//
//******************************************************************************

import java.io.IOException;

/**
 * Class decrypts a given ciphertext file by using ARK3 in cipher feedback mode.
 * It works by decrypting raw bytes within the file. This means that this can
 * decrypt any kind of file that was encrypted with Encrypt.java.
 * <P><TT>usage: java Decrypt &lt;ctfile&gt; &lt;ptfile&gt; 
 * &lt;key&gt; &lt;iv&gt;</TT></P>
 * 
 * @author Jimi Ford (jhf3617)
 * @version 3-21-2015
 */
public class Decrypt {

	// private data members
	private static final int CT_INDEX = 0;
	private static final int PT_INDEX = 1;
	private static final int KEY_INDEX = 2;
	private static final int NONCE_INDEX = 3;
	
	/**
	 * Program entry point
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		byte[] key = null, nonce = null, plaintext = null, ciphertext = null;
		try {
			if(args.length != 4) {
				throw new 
				SanitizationException("Incorrect number of arguments");
			}
			key = Sanitize.sanitizeKey(args[KEY_INDEX], "<key>");
			nonce = Sanitize.sanitizeNonce(args[NONCE_INDEX], "<iv>");
			ciphertext = FileIO.read(args[CT_INDEX]);
			plaintext = new byte[ciphertext.length];
		} catch (SanitizationException e) {
				error(e.getMessage());
		} catch (IOException e) {
			error("Problem reading \"" + args[CT_INDEX]+"\"");
		}
		ARK3CFB ark = new ARK3CFB();
		ark.setKey(KeyHelper.combineARK3KeyAndNonce(key, nonce));
		
		for(int i = 0; i < plaintext.length; i++) {
			plaintext[i] = (byte) (ark.decrypt(ciphertext[i] & 0xff) & 0xff);
		}
		
		try {
			FileIO.write(args[PT_INDEX], plaintext);
		} catch (IOException e) {
			error("Problem writing to \"" + args[PT_INDEX] + "\"");
		}
		
	}
	
	/**
	 * print usage error message and quit
	 */
	private static void usage() {
		System.err.println("usage: java Decrypt <ctfile> <ptfile> <key> <iv>");
		System.exit(1);
	}
	
	/**
	 * print error message then call <TT>usage()</TT>
	 * @param msg the error message to print
	 */
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
}
