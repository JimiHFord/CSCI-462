//******************************************************************************
//
// File:    Encrypt.java
// Package: ---
// Unit:    Class Encrypt
//
//******************************************************************************

import java.io.IOException;

 
public class Encrypt {

	private static final int PT_INDEX = 0;
	private static final int CT_INDEX = 1;
	private static final int KEY_INDEX = 2;
	private static final int NONCE_INDEX = 3;
	
	public static void main(String[] args) {
		byte[] key = null, nonce = null, plaintext = null, ciphertext = null;
		try {
			if(args.length != 4) {
				throw new 
				SanitizationException("Incorrect number of arguments");
			}
			key = Sanitize.sanitizeKey(args[KEY_INDEX], "<key>");
			nonce = Sanitize.sanitizeNonce(args[NONCE_INDEX], "<iv>");
			plaintext = FileIO.read(args[PT_INDEX]);
			ciphertext = new byte[plaintext.length];
		} catch (SanitizationException e) {
				error(e.getMessage());
		} catch (IOException e) {
			error("Problem reading \"" + args[PT_INDEX]+"\"");
		}
		ARK3CFB ark = new ARK3CFB();
		ark.setKey(KeyHelper.combineARK3KeyAndNonce(key, nonce));
		
		for(int i = 0; i < plaintext.length; i++) {
			ciphertext[i] = (byte) (ark.encrypt(plaintext[i] & 0xff) & 0xff);
		}
		
		try {
			FileIO.write(args[CT_INDEX], ciphertext);
		} catch (IOException e) {
			error("Problem writing to \"" + args[CT_INDEX] + "\"");
		}
		
	}
	
	private static void usage() {
		System.err.println("java Encrypt <ptfile> <ctfile> <key> <iv>");
		System.exit(1);
	}
	
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
}
