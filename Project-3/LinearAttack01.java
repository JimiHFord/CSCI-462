import edu.rit.util.Hex;
import java.util.Scanner;

/**
 * Do a linear attack on two-round PRESENT for two round 2 S-boxes' subkey bits.
 */
public class LinearAttack01 {
	// S-box bit width.
	private static final int S = 4;
	// 2^S.
	private static final int TWO_SUP_S = 1 << S;
	// S-box.
	private static final int[] Sbox = new int[] { 12, 5, 6, 11, 9, 0, 10, 13,
			3, 14, 15, 8, 4, 7, 1, 2 };
	// Inverse S-box.
	private static final int[] inverseSbox = new int[] { 5, 14, 15, 8, 12, 1,
			2, 13, 11, 4, 6, 3, 0, 7, 9, 10 };

	// Main program.
	public static void main(String[] args) {
		// Parse command line arguments.
//		if (args.length < 14)
//			usage();
		Oracle01 oracle = new Oracle01();
		int npt = oracle.count();
		int[] yBit = new int[S];
		for (int i = 0; i < S; ++i) {
			yBit[i] = Integer.parseInt(args[i + 3]);
		}
		int yMask = Hex.toInt(args[7]);
		int[] zBit = new int[S];
		for (int i = 0; i < S; ++i) {
			zBit[i] = Integer.parseInt(args[i + 8]);
		}
		int zMask = Hex.toInt(args[12]);
		int[] xBit = new int[args.length - 13];
		for (int i = 0; i < xBit.length; ++i) {
			xBit[i] = Integer.parseInt(args[i + 13]);
		}

		// Create counters.
		int[][] counter = new int[TWO_SUP_S][TWO_SUP_S];

		// Create oracle.
		

		// Process each plaintext-ciphertext pair.
		for (int p = 0; p < npt; ++p) {
			PtCt48 ptct = oracle.next();
			long plaintext = ptct.pt;
			long ciphertext = ptct.ct;

			// Compute input sum.
			int inputSum = 0;
			for (int i = 0; i < xBit.length; ++i)
				inputSum ^= (int) (plaintext >> xBit[i]) & 1;

			// Collect relevant bits of the ciphertext.
			int cty = 0;
			for (int i = 0; i < S; ++i)
				cty |= ((int) (ciphertext >> yBit[i]) & 1) << i;
			int ctz = 0;
			for (int i = 0; i < S; ++i)
				ctz |= ((int) (ciphertext >> zBit[i]) & 1) << i;

			// Try each guess for the final round subkey bits for S-box y.
			for (int ySubkey = 0; ySubkey < TWO_SUP_S; ++ySubkey) {
				// Compute output sum at input of S-box y.
				int ySum = 0;
				int y = inverseSbox[cty ^ ySubkey] & yMask;
				for (int i = 0; i < S; ++i)
					ySum ^= (y >> i) & 1;

				// Try each guess for the final round subkey bits for S-box z.
				for (int zSubkey = 0; zSubkey < TWO_SUP_S; ++zSubkey) {
					// Compute output sum at input of S-box z.
					int zSum = 0;
					int z = inverseSbox[ctz ^ zSubkey] & zMask;
					for (int i = 0; i < S; ++i)
						zSum ^= (z >> i) & 1;

					// If input sum + output sum = 0, increment subkey counter.
					if ((inputSum ^ ySum ^ zSum) == 0)
						++counter[ySubkey][zSubkey];
				}
			}
		}

		// Find maximum bias.
		double maxBias = 0.0;
		for (int ySubkey = 0; ySubkey < TWO_SUP_S; ++ySubkey) {
			for (int zSubkey = 0; zSubkey < TWO_SUP_S; ++zSubkey) {
				int ctr = counter[ySubkey][zSubkey];
				maxBias = Math.max(maxBias, bias(ctr, npt));
			}
		}

		// Print results.
		System.out.printf("Final round subkey bits%n");
		for (int i = S - 1; i >= 0; --i)
			System.out.printf("%3d", yBit[i]);
		for (int i = S - 1; i >= 0; --i)
			System.out.printf("%3d", zBit[i]);
		System.out.printf("       Count      |Bias|%n");
		for (int ySubkey = 0; ySubkey < TWO_SUP_S; ++ySubkey) {
			for (int zSubkey = 0; zSubkey < TWO_SUP_S; ++zSubkey) {
				for (int i = S - 1; i >= 0; --i)
					System.out.printf("%3d", (ySubkey >> i) & 1);
				for (int i = S - 1; i >= 0; --i)
					System.out.printf("%3d", (zSubkey >> i) & 1);
				int ctr = counter[ySubkey][zSubkey];
				double bias = bias(ctr, npt);
				System.out.printf("%12d%12.6f", ctr, bias);
				if (bias == maxBias)
					System.out.printf(" ***");
				System.out.println();
			}
		}
	}

	private static double bias(int ctr, int npt) {
		return Math.abs((double) ctr / (double) npt - 0.5);
	}

	private static void usage() {
		System.err
				.println("Usage: java LinearAttack01 <R> <npt> <seed> <y0> <y1> <y2> <y3> <ymask> <z0> <z1> <z2> <z3> <zmask> <x> ...");
		System.err.println("<R> = Number of rounds");
		System.err.println("<npt> = Number of known plaintexts");
		System.err.println("<seed> = Random seed");
		System.err
				.println("<y3><y2><y1><y0> = First S-box ciphertext bit positions");
		System.err.println("<ymask> = First S-box input bit mask (hex)");
		System.err
				.println("<z3><z2><z1><z0> = Second S-box ciphertext bit positions");
		System.err.println("<zmask> = Second S-box input bit mask (hex)");
		System.err.println("<x> = Plaintext bit positions");
		System.exit(1);
	}
}
