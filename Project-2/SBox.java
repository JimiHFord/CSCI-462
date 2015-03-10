import java.nio.ByteBuffer;

import edu.rit.util.Packing;


public class SBox {

	public static final short IRREDUCIBLE = //0x011D;
		// x8+x4+x3+x2+x0
		0b100011101; // USE THIS ONE FOR FINAL PROJECT
		// x8+x4+x3+x1+x0
//		0b100011011; // IN CLASS EXAMPLE
		// x8+x6+x5+x1+x0
//		0b101100011;
	
	public static long permute(long input) {
		byte[] bytes = new byte[8];
		Packing.unpackLongBigEndian(input, bytes, 0);
		byte temp = bytes[5];
		bytes[5] = bytes[4];
		bytes[4] = bytes[1];
		bytes[1] = bytes[0];
		bytes[0] = temp;
		
		temp = bytes[3];
		bytes[3] = bytes[6];
		bytes[6] = bytes[7];
		bytes[7] = bytes[2];
		bytes[2] = temp;
		return Packing.packLongBigEndian(bytes, 0);
	}
	
	public static int box(int i, int input) {
		int leftTerm = GF28.multiply(
				GF28.add(0b11000000, i), 
				input, IRREDUCIBLE);
		int rightTerm = 0b01100011;
		return GF28.add(leftTerm, rightTerm);
	}
}
