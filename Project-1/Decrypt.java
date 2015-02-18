import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;


public class Decrypt {


	private final int KEY_LENGTH = KeyHelper.KEY_LENGTH;
	private String cipherText;
	private int[] key;
	
	// Error checking is done in main
	// prevent construction
	private Decrypt() {
		
	}
	
	private Decrypt(int[] key, String cipherText) {
		this.key = key;
		this.cipherText = cipherText.toUpperCase();
	}

	public String plainText() {
		StringBuilder builder = new StringBuilder();
		Generator gen = new Generator(key);
		int P, C, C_X;
		for(int i = 0; i < cipherText.length(); i++) {
			C = cipherText.charAt(i) - 'A';
			if(C < KEY_LENGTH && C >= 0) {
				C_X = C - gen.next();
				C_X = C_X < 0 ? C_X + KEY_LENGTH : C_X;
				P = (C_X) % KEY_LENGTH;
				builder.append((char)(P + 'A'));
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
		} catch (IOException e) {
			displayError("The <ctfile> is either too large, or it was not found.");
		} catch (InvalidPathException e) {
			displayError("The <ctfile> is either too large, or it was not found.");
		}
		Decrypt decryptor = new Decrypt(key,cipherText);
		String plainText = decryptor.plainText();
		System.out.println(plainText);
		
	}
	
	private static void usage() {
		System.err.println("java Encrypt <key> <ptfile> <ctfile>");
		System.exit(1);
	}
	
	private static void displayError(String msg) {
		System.err.println(msg);
		usage();
	}
}
