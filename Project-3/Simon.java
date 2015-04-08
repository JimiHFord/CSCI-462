import edu.rit.util.Hex;

/**
 * Class is the implementation of Simon block cipher
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-8-2015
 */
public class Simon {

	/**
	 * number of rounds in reduced round Simon
	 */
	public final static int NUM_ROUNDS = 3;

	/**
	 * size of the block used in Simon
	 */
	public final static int BLOCK_SIZE = 48;

	/**
	 * size of the block pieces after being split up
	 */
	public final static int PIECE_SIZE = 24;

	private final static int MASK_24 = Mask.MASK_24;
	private final static long MASK_48 = Mask.MASK_48;
	
	public final static boolean FORWARD = true;
	public final static boolean BACKWARD = false;

	// private data members
	private final int[] subkey;

	public Simon(int[] threeSubkeys) {
		this.subkey = threeSubkeys;
	}

	public long encrypt(long pt) {
		int[] arr = new int[2];
		fill(arr, pt);
		round(arr, subkey[0], true);
		round(arr, subkey[1], true);
		round(arr, subkey[2], true);
		return pack(arr);
	}

	public long decrypt(long ct) {
		int[] arr = new int[2];
		fill(arr, ct);
		round(arr, subkey[2], false);
		round(arr, subkey[1], false);
		round(arr, subkey[0], false);
		return pack(arr);
	}

	private static void fill(int[] arr, long l) {
		arr[0] = (int) ((l >>> PIECE_SIZE) & MASK_24);
		arr[1] = (int) (l & MASK_24);
	}

	private static long pack(int[] arr) {
		return ((((long) arr[0]) << PIECE_SIZE) | ((long) arr[1])) & MASK_48;
	}

	public static void f(int[] arr, boolean forward) {
		if(forward) {
			int rotl1, rotl2, rotl8;
			int left = arr[0], right = arr[1];
			rotl1 = rotl24bit(left, 1);
			rotl2 = rotl24bit(left, 2);
			rotl8 = rotl24bit(left, 8);
			left = (((rotl1 & rotl8) ^ right) ^ rotl2);
			arr[1] = arr[0];
			arr[0] = left;
		} else {
			int rotr1, rotr2, rotr8, swap;
			swap = arr[0];
			arr[0] = arr[1];
			arr[1] = swap;
			// flipped
			int left = arr[0], right = arr[1];
			rotr1 = rotl24bit(left, 1);
			rotr2 = rotl24bit(left, 2);
			rotr8 = rotl24bit(left, 8);
			right ^= rotr2;
			right ^= (rotr1 & rotr8);
			arr[1] = right;
		}
	}
	
	public static long f(long in, boolean forward) {
		int[] arr = new int[2];
		fill(arr, in);
		f(arr, forward);
		return pack(arr);
	}
	
	public static long round(long in, int subkey, boolean forward) {
		int[] arr = new int[2];
		fill(arr, in);
		round(arr, subkey, forward);
		return pack(arr);
	}
	
	public static void round(int[] arr, int subkey, boolean forward) {
		f(arr, forward);
		arr[forward ? 0 : 1] ^= subkey;
	}
	
	public static long forwardRound(long in, int subkey) {
		return round(in, subkey, true);
	}
	
	public static long backwardsRound(long in, int subkey) {
		return round(in, subkey, false);
	}

	private static int rotl24bit(int src, int amt) {
		return rot24bit(src, amt, false);
	}

	private static int rot24bit(int src, int amt, boolean right) {
		int lAmt = right ? PIECE_SIZE - amt : amt;
		int rAmt = right ? amt : PIECE_SIZE - amt;
		int sL = src << lAmt;
		int sR = src >>> rAmt;
		return (sL | sR) & MASK_24;
	}

	// 16D64E9F444F 7E799E 0F65B9 8DEAB8
	// output: 00001107AB88C1BB
	// 178F860127E4 D602B2 4B7662 FDA03A
	// output: 0000D9AC5948864A
	// C7117352274B C69593 177EB2 4EE6FD
	// output: 0000CCD320E2B803
	// 96169406B093 8E01CE 9BDF55 E63DB9
	// output: 000085DFC8F77BEA
	public static void main(String[] args) {
		if (args.length != 4) {
			usage();
		}
		int[] subkeys = { Hex.toInt(args[1]), Hex.toInt(args[2]),
				Hex.toInt(args[3]) };
		long pt = Hex.toLong(args[0]);
		System.out.println(Hex.toString(pt).toUpperCase().substring(4, 16));
		Simon simon = new Simon(subkeys);
		long ct = simon.encrypt(pt);
		pt = simon.decrypt(ct);
		System.out.println(Hex.toString(ct).toUpperCase().substring(4, 16));
		System.out.println(Hex.toString(pt).toUpperCase().substring(4, 16));
	}

	private static void usage() {
		System.err
				.println("usage: java ReducedRoundSimon <pt> <sk1> <sk2> <sk3>");
		System.exit(1);
	}
}
