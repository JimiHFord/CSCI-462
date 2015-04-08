
public class Mask {

	public final static long MASK_48 = 0xffffffffffffL;
	public final static int  MASK_24 = 0xffffff;
	
	public static int chopHigh(long in) {
		return (int)(in >>> 24) & MASK_24;
	}
	
	public static int chopLow(long in) {
		return (int)(in) & MASK_24;
	}
}
