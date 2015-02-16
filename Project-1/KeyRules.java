import java.util.regex.Pattern;


public class KeyRules {

	public static final int KEY_LENGTH = 26;
	
	
	public static boolean isValidKey(String key) {
		key = key.toUpperCase();
		Pattern usagePattern = Pattern.compile("[A-Z]*");
		if(!usagePattern.matcher(key).matches()) {
			return false;
		}
		if(key.length() != KEY_LENGTH) {
			return false;
		}
		boolean[] flag = new boolean[KEY_LENGTH];
		for(int i = 0; i < key.length(); i++) {
			int c = key.charAt(i) - 'A';
			if(!flag[c]) {
				flag[c] = true;
			} else {
				return false;
			}
		}
		return true;
	}
}
