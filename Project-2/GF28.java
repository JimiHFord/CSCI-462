import edu.rit.util.Hex;

public class GF28 {

	
	
	public static int add(int a, int b) {
		return (a ^ b);
	}
	
	public static int multiply(final int a, final int b, 
			final int IRREDUCIBLE) {
		int mult = multiply(a,b);
		
		// mult now holds the two polynomials multiplied together
		// we have to reduce mult to be in the field of GF2^8
		int quotient = 0, remainder = mult;
		int largestRemainderPower = largestTermPower(remainder);
		int largestIrreduciblePower = largestTermPower(IRREDUCIBLE);
		while(largestRemainderPower >= Byte.SIZE) {
			quotient = largestRemainderPower - largestIrreduciblePower;
			int temp = multiply(maskPower(quotient), IRREDUCIBLE);
			remainder ^= temp;
			largestRemainderPower = largestTermPower(remainder);
		}
		
		return remainder;
	}
	
	private static int multiply(final int a, final int b) {
		return multiplyLimit(a,b,Short.SIZE);
	}
	
	private static int multiplyLimit(final int a, final int b, 
			final int limit) {
		int mult = 0;
		// a_power goes from 16 -> 0
		for(int a_power = Short.SIZE; a_power >= 0; a_power--) {
			// b_power goes from a_power -> 0
			for(int b_power = Short.SIZE; b_power >= 0; b_power--) {
				// multiply term together where appropriate
				if(match(a, a_power, b, b_power) && 
						a_power + b_power <= limit) {
					int power = a_power + b_power;
					mult ^= maskPower(power);
				}
			}
		}
		return mult;
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
