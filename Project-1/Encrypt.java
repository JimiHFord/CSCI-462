


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
			usage();
		}
		KeyHelper helper = new KeyHelper();
		if(!helper.isValidKey(args[0])) {
			incorrectKey();
		}
		int[] key = helper.initKey(args[0]);
		Encrypt encryptor = new Encrypt(key, 
				"In the beginning God created the heavens and the earth.\n" +
				"And the earth was without form and void,\n" +
				"and darkness was upon the face of the deep;\n" +
				"and the Spirit of God was moving over the face of the waters.");
		String cipherText = encryptor.cipherText();
		System.out.println(cipherText);
		
	}
	
	private static void usage() {
		System.err.println("java Encrypt <key> <ptfile> <ctfile>");
		System.exit(1);
	}
	
	private static void incorrectKey() {
		System.err.println("The <key> must only consist of 26 unique letters of the english alphabet.");
	}
}
