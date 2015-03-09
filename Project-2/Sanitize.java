import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.rit.util.Hex;


public class Sanitize {

	public static final int KEY_STRING_LENGTH = 32;
	public static final int IV_STRING_LENGTH = 16;
	
	public static byte[] sanitizeKey(String key, String argName) 
		throws SanitizationException {
//		long key = Packing.packLongBigEndian(args[KEY_INDEX].getBytes(), 0);
		Pattern p = Pattern.compile("^[0-9a-fA-F]{" + KEY_STRING_LENGTH+"}");
		Matcher m = p.matcher(key);
		if(!m.matches())
			throw new SanitizationException(
				String.format("The %1s must be %d hexadecimal characters.",
						argName, KEY_STRING_LENGTH));
		
		return Hex.toByteArray(key);
	}
	
	public static byte[] sanitizeNonce(String nonce, String argName)
		throws SanitizationException {
		Pattern p = Pattern.compile("^[0-9a-fA-F]{" + IV_STRING_LENGTH+"}");
		Matcher m = p.matcher(nonce);
		if(!m.matches())
			throw new SanitizationException(
				String.format("The %1s must be %d hexadecimal characters.",
						argName, IV_STRING_LENGTH));
		return Hex.toByteArray(nonce);
	}
}
