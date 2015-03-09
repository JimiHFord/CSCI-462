import java.nio.ByteBuffer;


public class SBox {

	public static final short IRREDUCIBLE = //0x011D;
		0b100011101;
	
	public static long permute(long input) {
		byte[] bytes = ByteBuffer.allocate(8).
				putLong(input).array();
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
		
		return ByteBuffer.wrap(bytes).getLong();
	}
}
