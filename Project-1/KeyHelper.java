import java.util.regex.Pattern;


public class KeyHelper {

	public static final int KEY_LENGTH = 26;
	
	
	public boolean isValidKey(String key) {
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
	
	public int[] initKey(String input) {
		input = input.toUpperCase();
		int[] retval = new int[KEY_LENGTH];
		int[] converted = new int[KEY_LENGTH];
		int j = 0;
		int temp;
		for(int i = 0; i < retval.length; i++) {
			retval[i] = i;
		}
		if(isValidKey(input)) {
			for(int i = 0; i < input.length(); i++) {
				
				retval[i] = input.indexOf(i + 'A');
			}
			for(int i = 0; i < KEY_LENGTH; i++) {
				j = (j + converted[i]) % (KEY_LENGTH + 1);
				temp = retval[i];
				retval[i] = retval[j];
				retval[j] = temp;
			}
		}
		return retval;
	}
}
