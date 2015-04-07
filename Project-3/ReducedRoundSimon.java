import edu.rit.util.Hex;

public class ReducedRoundSimon {

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

	private final static int MASK_24 = 0xffffff;
	private final static long MASK_48 = 0xffffffffffffL;

	// private data members
	private int[] subkey;

	public ReducedRoundSimon(int[] threeSubkeys) {
		this.subkey = threeSubkeys;
	}

	public long encrypt(long pt) {
		int[] arr = new int[2];
		fill(arr, pt);
		for (int i = 0; i < NUM_ROUNDS; i++) {
			forwardRound(arr, subkey[i]);
		}
		return pack(arr);
	}

	public long decrypt(long ct) {
		int[] arr = new int[2];
		fill(arr, ct);
		for(int i = subkey.length -1; i >= 0; i--) {
			backwardsRound(arr, subkey[i]);
		}
		return pack(arr);
	}
	
	private void fill(int[] arr, long l) {
		arr[0] = (int) ((l >>> PIECE_SIZE) & MASK_24);
		arr[1] = (int) (l & MASK_24);
	}
	
	private long pack(int[] arr) {
		return ((((long) arr[0]) << PIECE_SIZE) | ((long) arr[1])) & MASK_48;
	}
	
	private void forwardRound(int[] state, int subkey) {
		int rotl1, rotl2, rotl8, swap;
		int left = state[0], right = state[1];
		rotl1 = rotl24bit(left, 1);
		rotl2 = rotl24bit(left, 2);
		rotl8 = rotl24bit(left, 8);
		swap = (((rotl1 & rotl8) ^ right) ^ rotl2) ^ (subkey);
		state[1] = state[0];
		state[0] = swap;
	}
	
	private void backwardsRound(int[] state, int subkey) {
		int rotr1, rotr2, rotr8, swap;
		swap = state[0];
		state[0] = state[1];
		state[1] = swap;
		// flipped
		int left = state[0] & MASK_24, right = state[1] & MASK_24;
		rotr1 = rotr24bit(left, 1);
		rotr2 = rotr24bit(left, 2);
		rotr8 = rotr24bit(left, 8);
		right ^= (subkey & MASK_24);
		right ^= rotr2;
		right ^= (rotr1 & rotr8);
//		state[0] = left;
		state[1] = right;
	}

	private int rotr24bit(int src, int amt) {
		return rotl24bit(src, -1 * amt);
	}
	
	private int rotl24bit(int src, int amt) {
		int lAmt = amt;
		int rAmt = PIECE_SIZE - amt;
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
		System.out.println(Hex.toString(pt).toUpperCase());
		ReducedRoundSimon simon = new ReducedRoundSimon(subkeys);
		long ct = simon.encrypt(pt);
		pt = simon.decrypt(ct);
		System.out.println(Hex.toString(ct).toUpperCase());
		System.out.println(Hex.toString(pt).toUpperCase());
	}

	private static void usage() {
		System.err
				.println("usage: java ReducedRoundSimon <pt> <sk1> <sk2> <sk3>");
		System.exit(1);
	}
}
