import edu.rit.util.Hex;


public class GF28 {

	
	
	public static int add(int a, int b) {
		return (a ^ b);
	}
	
	public static int multiply(final int a, final int b, 
			final int IRREDUCIBLE) {
		int a_cp = a, b_cp = b;
		int mult = multiply(a,b);
		
		// mult now holds the two polynomials multiplied together
		// we have to reduce mult to be in the field of GF2^8
		int quotient = 0, remainder = mult;
		int powerFactor = 0;
		int largestRemainderPower = largestTermPower(remainder);
		int largestIrreduciblePower = largestTermPower(IRREDUCIBLE);
		while(largestRemainderPower >= Byte.SIZE) {
			
			largestRemainderPower = nextLargest(largestRemainderPower);
		}
		
		return mult;
	}
	
	private static int multiply(final int a, final int b) {
		return multiplyLimit(a,b,Short.SIZE-1);
	}
	
	private static int multiplyLimit(final int a, final int b, 
			final int limit) {
		int mult = 0;
		// a_power goes from 15 -> 0
		for(int a_power = Short.SIZE-1; a_power >= 0; a_power--) {
			// b_power goes from a_power -> 0
			for(int b_power = a_power; b_power >= 0; b_power--) {
				// multiply term together where appropriate
				if(match(a, a_power, b, b_power) && 
						a_power + b_power <= limit) {
					mult ^= maskPower(a_power + b_power);
				}
			}
		}
		return mult;
	}
	
	private static int nextLargest(int term) {
		return largestTermPower(term,term);
	}
	
	private static int largestTermPower(int term, int limit) {
		int power = -1;
		for(int i = 0; i < limit; i++) {
			if(matchPower(term, i)) {
				power = i;
			}
		}
		return power;
	}
	
	private static int largestTermPower(int term) {
		return largestTermPower(term, Short.SIZE);
	}
	
	private static boolean match(int a, int a_power, 
			int b, int b_power) {
		return matchPower(a, a_power) && matchPower(b, b_power);
	}
	
	private static boolean match(int a, int b) {
		return (a & b) != 0;
	}
	
	private static int maskPower(int i) {
		return 1 << i;
	}
	
	private static boolean matchPower(int a, int power) {
		return match(a, maskPower(power));
	}
}
