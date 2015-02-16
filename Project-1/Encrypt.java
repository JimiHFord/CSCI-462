


public class Encrypt {

	
	
	public Encrypt(char[] key) {
		
	}
	
	public static void main(String[] args) {
		if(args.length != 3) {
			usage();
		}
		KeyRules helper = new KeyRules();
		if(!helper.isValidKey(args[0])) {
			incorrectKey();
		}
		int[] key = helper.initKey(args[0]);
		for(int k : key) {
			System.out.print(k + ", ");
		}System.out.println();
	}
	
	private static void usage() {
		System.err.println("java Encrypt <key> <ptfile> <ctfile>");
		System.exit(1);
	}
	
	private static void incorrectKey() {
		System.err.println("The <key> must only consist of 26 unique letters of the english alphabet.");
	}
}
