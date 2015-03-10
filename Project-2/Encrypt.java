import edu.rit.util.Hex;





 
public class Encrypt {

	public static final int KEY_INDEX = 2;
	public static final int NONCE_INDEX = 3;
	
	public static void main(String[] args) {
		byte[] key = null, nonce = null;
		try {
			if(args.length != 4) {
				throw new SanitizationException("Incorrect number of arguments");
			}
			key = Sanitize.sanitizeKey(args[KEY_INDEX], "<key>");
			nonce = Sanitize.sanitizeNonce(args[NONCE_INDEX], "<iv>");
		} catch (SanitizationException e) {
			System.err.println(e.getMessage());
			usage();
		}
		ARK3 ark = new ARK3(key,nonce);
//		System.out.println(ark.keyStateString());
		// x6+x5+x2+x0 * x7+x6+x4+x2+x1
//		byte m = (byte)(GF28.multiply(0b01100101, 0b11010110, SBox.IRREDUCIBLE) & 0xff);
		// x7 * x7
		byte m = (byte)(GF28.multiply(0b10000000, 0b10000000, SBox.IRREDUCIBLE) & 0xff);
		// x7+x6+x4 * x6+x5+x0
//		byte m = (byte)(GF28.multiply(0b11010000, 0b01100001, SBox.IRREDUCIBLE) & 0xff);
		System.out.println(Hex.toString(m));
//		System.out.println(Hex.toString(1));
	}
	
	private static void usage() {
		System.err.println("java Encrypt <ptfile> <ctfile> <key> <iv>");
		System.exit(1);
	}
}
