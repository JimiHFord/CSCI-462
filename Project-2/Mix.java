
public class Mix {

	public static long mix(long input) {
		
		byte a, b;
		
		
		return 0;
	}
	
	private static int mix(final int a, final int b) {
		int c, d;
		c = 0xff & GF28.add(
				GF28.multiply(0b00000010, a, SBox.IRREDUCIBLE),b);
		
		d = 0xff & GF28.add(a, 
				GF28.multiply(0b00000011, b, SBox.IRREDUCIBLE));
		return (c << 8) | d;
	}
}
