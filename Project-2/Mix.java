import edu.rit.util.Packing;


public class Mix {

	public static long mix(long input) {
		byte[] bytes = new byte[8];
		Packing.unpackLongBigEndian(input, bytes, 0);
		long mix_1, mix_2, mix_3, mix_4;
		mix_1 = mix(bytes[0], bytes[1]) << (16 * 3);
		mix_2 = mix(bytes[2], bytes[3]) << (16 * 2);
		mix_3 = mix(bytes[4], bytes[5]) << (16 * 1);
		mix_4 = mix(bytes[6], bytes[7]);
		return mix_1 | mix_2 | mix_3 | mix_4;
	}
	
	private static long mix(final byte a, final byte b) {
		long c, d;
		c = 0xff & GF28.add(
				GF28.multiply(0b00000010, a, SBox.IRREDUCIBLE),b);
		
		d = 0xff & GF28.add(a, 
				GF28.multiply(0b00000011, b, SBox.IRREDUCIBLE));
		return (c << 8) | d;
	}
}
