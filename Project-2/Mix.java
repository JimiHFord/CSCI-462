import edu.rit.util.Hex;


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
				GF28.multiply(0b00000010, a & 0xff, SBox.IRREDUCIBLE),b & 0xff));
		
		d = (byte) (0xff & GF28.add(a & 0xff, 
				GF28.multiply(0b00000011, b & 0xff, SBox.IRREDUCIBLE)));
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
