import edu.rit.util.Hex;

public class GF28 {

	
	
	public static int add(int a, int b) {
		return (a ^ b);
	}
	
	public static int multiply(int a, int b, final int IRREDUCIBLE) {
		final int p = IRREDUCIBLE;
		int result = 0;
		for (int bit = 0x80; bit > 0; bit >>= 1) {
		    result <<= 1;
		    if ((result & 0x100) != 0) {
		    	result ^= p;
		    }
		    if ((b & bit) != 0) {
		    	result ^= a;
		    }
	    }
		return result;
	}
	
	public static void main(String[] args) {
		// x8 + x4 + x3 + x + 1
		int IRREDUCIBLE = 0b100011011;
		// x7 + x4 + x2 + 1
		int a = 0b10010101;
		// x5 + x4 + 1 
		int b = 0b00110001;
		// ANSWER: x5 + x4 + x3 + x + 1
		int answer = multiply(a, b, IRREDUCIBLE);
		System.out.println(Hex.toString(answer)); // WORKS!!!
	}
}
