import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.rit.util.Hex;
import edu.rit.util.Packing;





 
public class Encrypt {

	public static final int PT_INDEX = 0;
	public static final int CT_INDEX = 1;
	public static final int KEY_INDEX = 2;
	public static final int NONCE_INDEX = 3;
	
	public static void main(String[] args) {
		byte[] key = null, nonce = null, plaintext = null, ciphertext = null;
		byte a = (byte)0xff;
		try {
			if(args.length != 4) {
				throw new SanitizationException("Incorrect number of arguments");
			}
			key = Sanitize.sanitizeKey(args[KEY_INDEX], "<key>");
			nonce = Sanitize.sanitizeNonce(args[NONCE_INDEX], "<iv>");
//			plaintext = Files.readAllBytes(Paths.get(args[PT_INDEX]));
//			ciphertext = new byte[plaintext.length];
		} catch (SanitizationException /*| IOException*/ | InvalidPathException e) {
			System.err.println(e.getMessage());
			usage();
		} 
		ARK3 ark = new ARK3(key);
		String plaintextString = 
				"496e2074686520626567696e6e696e67"+
				"207761732074686520576f72642c2061"+
			 	"6e642074686520576f72642077617320"+
			 	"7769746820476f642c20616e64207468"+
			 	"6520576f72642077617320476f642e0a";
		
		byte[] plaintext_hex_array = new byte[] {
			0x49, 0x6e, 0x20, 0x74, 0x68, 0x65, 0x20, 0x62, 
			0x65, 0x67, 0x69, 0x6e, 0x6e, 0x69, 0x6e, 0x67,
		    0x20, 0x77, 0x61, 0x73, 0x20, 0x74, 0x68, 0x65, 
		    0x20, 0x57, 0x6f, 0x72, 0x64, 0x2c, 0x20, 0x61,
		    0x6e, 0x64, 0x20, 0x74, 0x68, 0x65, 0x20, 0x57, 
		    0x6f, 0x72, 0x64, 0x20, 0x77, 0x61, 0x73, 0x20,
		    0x77, 0x69, 0x74, 0x68, 0x20, 0x47, 0x6f, 0x64, 
		    0x2c, 0x20, 0x61, 0x6e, 0x64, 0x20, 0x74, 0x68,
		    0x65, 0x20, 0x57, 0x6f, 0x72, 0x64, 0x20, 0x77, 
		    0x61, 0x73, 0x20, 0x47, 0x6f, 0x64, 0x2e, 0x0a
		};
		System.out.println("plaintext_hex_array:");
		System.out.println(new String(plaintext_hex_array));
		plaintext = Hex.toByteArray(plaintextString);
		String correctString = 
				"0ed8b97674929eafa3042cb200f8d57d"+
			    "cb22b75d25ea56edad82434c4521a241"+
			    "4d0fbf56c60744d02e46b201c251bb24"+
			    "d3af561b4761147d2bb245ab0df14921"+
			    "67e3bf202af6d258bf3a6e48e298307d";
		byte[] correct = Hex.toByteArray(correctString);
		ciphertext = new byte[plaintext.length];
//		int num64BitBlocks = (int) Math.ceil(plaintext.length / 64.0);
		long packedNonce = Packing.packLongBigEndian(nonce, 0);
		long stream = ark.roundify(packedNonce);
		long plaintextLong = 0;
		long ciphertextLong = 0;
//		byte[] block = new byte[8];
		System.out.println(Hex.toString(stream));
		for(int blockIndex = 0; blockIndex < plaintext.length; blockIndex+=8) {
			plaintextLong = Packing.packLongBigEndian(plaintext, blockIndex);
			ciphertextLong = plaintextLong ^ stream;
			Packing.unpackLongBigEndian(ciphertextLong, ciphertext, blockIndex);
			stream = ark.roundify(ciphertextLong);
		}
		StringBuilder builder = new StringBuilder();
		String cipherString = Hex.toString(ciphertext);
		for(int i = 0; i < cipherString.length(); i+=2) {
			if(i % 32 == 0) builder.append('\n');
			builder.append(cipherString.substring(i, i+2) + " ");
			
		}
//		System.out.println(builder.toString());
//		System.out.println(Hex.toString(ciphertext));
		System.out.println(new String(plaintext));
		int div = 8;
		for(int i = 0; i < ciphertext.length; i++) {
			if(i % div == 0) System.out.println();
			byte temp = ciphertext[i];
			if(ciphertext[i] != correct[i]) {
				System.out.print("("+Hex.toString(temp)+")");
			} else {
				System.out.print(" " + Hex.toString(temp) + " ");
			}
		}
		System.out.println();
		for(int i = 0; i < ciphertext.length; i++) {
			if(i % div == 0) System.out.println();
			byte temp = correct[i];
			if(ciphertext[i] != correct[i]) {
				System.out.print("["+Hex.toString(temp)+"]");
			} else {
				System.out.print(" " + Hex.toString(temp) + " ");
			}
		}
	}
	
	private static void usage() {
		System.err.println("java Encrypt <ptfile> <ctfile> <key> <iv>");
		System.exit(1);
	}
}
