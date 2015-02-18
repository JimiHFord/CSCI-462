import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;




public class Encrypt {

	private final int KEY_LENGTH = KeyHelper.KEY_LENGTH;
	private String plainText;
	private int[] key;
	
	// Error checking is done in main
	// prevent construction
	private Encrypt() {
		
	}
	
	private Encrypt(int[] key, String plainText) {
		this.key = key;
		this.plainText = plainText.toUpperCase();
	}
	
	/*
	 * 
	 * def run_rc4(k, text):
		    cipher_chars = []
		    random_byte_gen = gen_random_bytes(k)
		    for char in text:
		        byte = ord(char)
		        cipher_byte = byte ^ random_byte_gen.next()
		        cipher_chars.append(chr(cipher_byte))
		    return ''.join(cipher_chars)
	 */
	public String cipherText() {
		StringBuilder builder = new StringBuilder();
		Generator gen = new Generator(key);
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
	
	public static void main(String[] args) {
		if(args.length != 3) {
			SharedErrorOutput.usage();
		}
		KeyHelper helper = new KeyHelper();
		if(!helper.isValidKey(args[0])) {
			SharedErrorOutput.displayError("The <key> must only consist of 26 unique letters of the english alphabet.");
		}
		int[] key = helper.initKey(args[0]);
		String plainText = null;
		try {
			plainText = new String(Files.readAllBytes(Paths.get(args[1])));
		} catch (IOException e) {
			SharedErrorOutput.displayError("The <ctfile> is either too large, or it was not found.");
		} catch (InvalidPathException e) {
			SharedErrorOutput.displayError("The <ctfile> is either too large, or it was not found.");
		}
		Encrypt encryptor = new Encrypt(key, plainText);
		String cipherText = encryptor.cipherText();
		System.out.println(cipherText);
	}
}
