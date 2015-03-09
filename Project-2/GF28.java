import edu.rit.util.Hex;


public class GF28 {

	
	
	public static byte add(byte a, byte b) {
		return (byte) (a ^ b);
	}
	
	public static byte multiply(final byte a, final byte b, 
			final short IRREDUCIBLE) {
		byte a_cp = a, b_cp = b;
		final byte[] mask = { 
			0b00000001,
			0b00000010,
			0b00000100,
			0b00001000,
			0b00010000,
			0b00100000,
			0b01000000,
	 (byte) 0b10000000
		};
		boolean[] a_arr = new boolean[Byte.SIZE];
		boolean[] b_arr = new boolean[Byte.SIZE];
		boolean[] ip_arr= new boolean[Short.SIZE];
		boolean[] remainder = new boolean[Short.SIZE];
		for(int i = 0; i < Byte.SIZE; i++) {
			a_arr[i] = (a & mask[i]) != 0;
			b_arr[i] = (b & mask[i]) != 0;
			ip_arr[i]= (IRREDUCIBLE & mask[i]) != 0;
		}
		for(int i = Byte.SIZE; i < Short.SIZE; i++) {
			ip_arr[i] = (IRREDUCIBLE & (1 << i)) != 0;
		}
		
		// i goes from 7 -> 0
		for(int i = Byte.SIZE -1; i >= 0; i--) {
			for(int j = i; j >= 0; j--) {
				// multiply term together where appropriate
				if(a_arr[i] && b_arr[j]) {
					remainder[i + j] = !remainder[i + j];
				}
			}
		}
		boolean[] q = new boolean[Short.SIZE];
		// loop from 15 to 8 to get rid of unwanted terms
		for(int power = Short.SIZE-1; power >= Byte.SIZE; power--) {
			// i goes from 15 - 0 inclusive
			int index = Short.SIZE-1-power;
			if(remainder[index]){
				// then we have to get rid of this term
				
			}
		}
		
		short mult = 0;
		for(int i = Short.SIZE-1; i >= 0; i--) {
			if(remainder[i]) {
				mult |= 1 << (i);
			}
		}
		// mult now holds the two polynomials multiplied
		// together
		
		System.out.println(Hex.toString(mult));
		return 0;
	}
	
	
	private static boolean[] xor(boolean[] a, boolean[] b) {
		if(a.length != b.length) return null;
		boolean[] retval = new boolean[a.length];
		for(int i=0; i< a.length; i++) {
			retval[i] = a[i] ^ b[i];
		}
		return retval;
	}
}
