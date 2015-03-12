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
	 * 
	 */
	
	public static void permute(byte[] bytes) {
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
	}
	
//	public static long permute(long input) {
//		
//		long byte_0, byte_1, byte_2, byte_3, byte_4,
//			byte_5, byte_6, byte_7, tmp; 
//		// QUESTION: not reusing Packing.unpackLong
//		// lose points?
//		
//		byte[] bytes = new byte[8];
//		Packing.unpackLongBigEndian(input, bytes, 0);
//		byte temp = bytes[5];
//		bytes[5] = bytes[4];
//		bytes[4] = bytes[1];
//		bytes[1] = bytes[0];
//		bytes[0] = temp;
//		
//		temp = bytes[3];
//		bytes[3] = bytes[6];
//		bytes[6] = bytes[7];
//		bytes[7] = bytes[2];
//		bytes[2] = temp;
//		
//		byte_0 = (input >>> 8 * 7) & 0xff;
//		byte_1 = (input >>> 8 * 6) & 0xff;
//		byte_2 = (input >>> 8 * 5) & 0xff;
//		byte_3 = (input >>> 8 * 4) & 0xff;
//		byte_4 = (input >>> 8 * 3) & 0xff;
//		byte_5 = (input >>> 8 * 2) & 0xff;
//		byte_6 = (input >>> 8 * 1) & 0xff;
//		byte_7 = (input >>> 8 * 0) & 0xff;
//		
//		tmp = byte_5;
//		byte_5 = byte_4;
//		byte_4 = byte_1;
//		byte_1 = byte_0;
//		byte_0 = tmp;
//		
//		tmp = byte_3;
//		byte_3 = byte_6;
//		byte_6 = byte_7;
//		byte_7 = byte_2;
//		byte_2 = tmp;
//		
//		byte_0 <<= 8 * 7;
//		byte_1 <<= 8 * 6;
//		byte_2 <<= 8 * 5;
//		byte_3 <<= 8 * 4;
//		byte_4 <<= 8 * 3;
//		byte_5 <<= 8 * 2;
//		byte_6 <<= 8 * 1;
//		byte_7 <<= 8 * 0;
//		
//		return byte_0 | byte_1 | byte_2 | byte_3 |
//			   byte_4 | byte_5 | byte_6 | byte_7;
//	}
	
	public static void box(byte[] bytes) {
		// QUESTION: not reusing Packing.unpack
		// lose points?
		
		bytes[0] = box(1, bytes[0]);
		bytes[1] = box(2, bytes[1]);
		bytes[2] = box(3, bytes[2]);
		bytes[3] = box(4, bytes[3]);
		bytes[4] = box(5, bytes[4]);
		bytes[5] = box(6, bytes[5]);
		bytes[6] = box(7, bytes[6]);
		bytes[7] = box(8, bytes[7]);
	}
	
	private static byte box(int i, byte input) {
		int leftTerm = GF28.multiply(
				GF28.add(0b11000000, i & 0xff), 
				input & 0xff, IRREDUCIBLE) & 0xff;
		int rightTerm = 0b01100011;
		return (byte)(GF28.add(leftTerm, rightTerm) & 0xff);
	}
}
