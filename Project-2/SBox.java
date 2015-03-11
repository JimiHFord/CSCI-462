//import java.nio.ByteBuffer;

import edu.rit.util.Packing;


public class SBox {

	public static final short IRREDUCIBLE = //0x011D;
		// x8+x4+x3+x2+x0
		0b100011101; // USE THIS ONE FOR FINAL PROJECT
		// x8+x4+x3+x1+x0
//		0b100011011; // IN CLASS EXAMPLE
		// x8+x6+x5+x1+x0
//		0b101100011;
	
	/*
	 * byte[] bytes = new byte[8];
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
	 */
	
	public static long permute(long input) {
		
		long byte_0, byte_1, byte_2, byte_3, byte_4,
			byte_5, byte_6, byte_7, tmp; 
		// note to self tried without the & mask
		// and did not work
		byte_0 = (input >>> 8 * 7) & 0xff;
		byte_1 = (input >>> 8 * 6) & 0xff;
		byte_2 = (input >>> 8 * 5) & 0xff;
		byte_3 = (input >>> 8 * 4) & 0xff;
		byte_4 = (input >>> 8 * 3) & 0xff;
		byte_5 = (input >>> 8 * 2) & 0xff;
		byte_6 = (input >>> 8 * 1) & 0xff;
		byte_7 = (input >>> 8 * 0) & 0xff;
		
		tmp = byte_5;
		byte_5 = byte_4;
		byte_4 = byte_1;
		byte_1 = byte_0;
		byte_0 = tmp;
		
		tmp = byte_3;
		byte_3 = byte_6;
		byte_6 = byte_7;
		byte_7 = byte_2;
		byte_2 = tmp;
		
		byte_0 <<= 8 * 7;
		byte_1 <<= 8 * 6;
		byte_2 <<= 8 * 5;
		byte_3 <<= 8 * 4;
		byte_4 <<= 8 * 3;
		byte_5 <<= 8 * 2;
		byte_6 <<= 8 * 1;
		byte_7 <<= 8 * 0;
		
		return byte_0 | byte_1 | byte_2 | byte_3 |
			   byte_4 | byte_5 | byte_6 | byte_7;
	}
	
	public static long box(long input) {
		int s_1, s_2, s_3, s_4, s_5, s_6, s_7, s_8;
		s_1 = (int) (input >>> 8 * 7 & 0xff);
		s_2 = (int) (input >>> 8 * 6 & 0xff);
		s_3 = (int) (input >>> 8 * 5 & 0xff);
		s_4 = (int) (input >>> 8 * 4 & 0xff);
		s_5 = (int) (input >>> 8 * 3 & 0xff);
		s_6 = (int) (input >>> 8 * 2 & 0xff);
		s_7 = (int) (input >>> 8 * 1 & 0xff);
		s_8 = (int) (input >>> 8 * 0 & 0xff);
		s_1 = box(1, s_1);
		s_2 = box(2, s_2);
		s_3 = box(3, s_3);
		s_4 = box(4, s_4);
		s_5 = box(5, s_5);
		s_6 = box(6, s_6);
		s_7 = box(7, s_7);
		s_8 = box(8, s_8);
		long result = 0;
		result |= s_1;
		result <<= 8;
		result |= s_2;
		result <<= 8;
		result |= s_3;
		result <<= 8;
		result |= s_4;
		result <<= 8;
		result |= s_5;
		result <<= 8;
		result |= s_6;
		result <<= 8;
		result |= s_7;
		result <<= 8;
		result |= s_8;
		return result;
	}
	
	private static int box(int i, int input) {
		int leftTerm = GF28.multiply(
				GF28.add(0b11000000, i), 
				input, IRREDUCIBLE);
		int rightTerm = 0b01100011;
		return GF28.add(leftTerm, rightTerm);
	}
}
