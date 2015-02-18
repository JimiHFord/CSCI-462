
public class SharedErrorOutput {

	
	public static void usage() {
		System.err.println("java Encrypt <key> <ptfile> <ctfile>");
		System.exit(1);
	}
	
	public static void displayError(String msg) {
		System.err.println(msg);
		usage();
	}
}
