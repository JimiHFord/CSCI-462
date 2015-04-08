public class Oracle01 {

	private int current = 0;
	private PtCt48[] pairs = new PtCt48[] {
			new PtCt48(0x1D6B6CBDDFE0L, 0xB475EB754C85L),
			new PtCt48(0xC2F7F32BD8B9L, 0x15BB1F6CCA85L),
			new PtCt48(0x2E5FB44DB9CEL, 0xD80A12AAB3C6L),
			new PtCt48(0x1BE680E52187L, 0xD47F6ACEA914L),
			new PtCt48(0x1D0AC266FA88L, 0x8DA68A1BF96AL),
			new PtCt48(0x2A218D45214EL, 0xBDAD88F756E2L),
			new PtCt48(0x539299289F5EL, 0x59623DE9FDB0L),
			new PtCt48(0xF96AB97683A2L, 0xF6A3DB7A2CDAL),
			new PtCt48(0xB35FBB4559B2L, 0x26642AE883AEL),
			new PtCt48(0xEF4DB32C9952L, 0x6923CD5B53F0L) };

	public PtCt48 next() {
		if (current == pairs.length)
			current = 0;
		return pairs[current++];
	}

	public int count() {
		return pairs.length;
	}
}
