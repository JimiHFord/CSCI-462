import edu.rit.util.Hex;
import edu.rit.util.Packing;


public class Mix {

	public static void mix(byte[] bytes) {
		mix(0, bytes);
		mix(1, bytes);
		mix(2, bytes);
		mix(3, bytes);
	}
	
	private static void mix(int pair, byte[] bytes) {
		byte a, b, c, d;
		int p0 = pair*2;
		int p1 = p0 + 1;
		a = bytes[p0];
		b = bytes[p1];
		// QUESTION: how does java treat shifting bits beyond how many
		// bits there are to shift?
		// ANSWER: see main
		c = (byte) (0xff & GF28.add(
				GF28.multiply(0b00000010, a, SBox.IRREDUCIBLE),b));
		
		d = (byte) (0xff & GF28.add(a, 
				GF28.multiply(0b00000011, b, SBox.IRREDUCIBLE)));
		bytes[p0] = c;
		bytes[p1] = d;
	}
	
	public static void main(String[] args) {
		int x = 1;
		long y = 0x8000000000000000L;
		for(int i = 0; i < Long.SIZE; i++) {
//			System.out.println(Hex.toString((long)x << i));
			System.out.println(Hex.toString(y >>> i));
		}
	}
}
