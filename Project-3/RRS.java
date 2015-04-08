import edu.rit.util.Hex;


public class RRS {

	public static void main(String[] args) {
		ReducedRoundSimon simon = new ReducedRoundSimon(null);
		String print = null;
		long r1 = 0, r2 = 0, r3 = 0;
		long t1 = 0, t2 = 0, t3 = 0;
		long i_ = 0;
		for(long i = 0x1; i < 0xffffffffffffl; i<<=1) {
			i_|=i;
			r1 = simon.forwardRound(i, 0);
			t1 |= r1;
			r2 = simon.forwardRound(r1, 0);
			t2 |= r2;
			r3 = simon.forwardRound(r2, 0);
			t3 |= r3;
			print = to48String(i) + '\t' +
					to48String(r1) + '\t' +
					to48String(r2) + '\t' +
					to48String(r3);
			System.out.println(print);
		}
		print = to48String(i_) + '\t' +
				to48String(t1) + '\t' +
				to48String(t2) + '\t' +
				to48String(t3);
		System.out.println(print);
//		for(long i = 0xffffffffffffl; (i&0x800000000000l) != 0 ; 
//				i= (i<<1)&0xffffffffffffL) {
//			r1 = simon.forwardRound(i, 0);
//			r2 = simon.forwardRound(r1, 0);
//			r3 = simon.forwardRound(r2, 0);
//			print = to48String(i) + '\t' +
//					to48String(r1) + '\t' +
//					to48String(r2) + '\t' +
//					to48String(r3);
//			System.out.println(print);
//		}
//		long count = 0;
//		RRS wd = new RRS(System.currentTimeMillis());
//		for(long i = 0; i < 0xffffffffffffl; i++) {
////			r1 = simon.forwardRound(i, 0);
////			r2 = simon.forwardRound(r1, 0);
////			r3 = simon.forwardRound(r2, 0);
//			count++;
//			if(wd.secondPassed(System.currentTimeMillis())) {
//				System.out.println(count);
//				count = 0;
//			}
//		}
//		System.out.println(count);
//		System.currentTimeMillis()
	}
	
	private static String to48String(long l) {
		return l == 0 ? Hex.toString(l)+",0" :
			Hex.toString(l).substring(4, 16)+","+
				Long.bitCount(l);
	}
	
//	public class RRS {
		private long current;
		public RRS(long current) {
			this.current = current;
		}
		
		public boolean secondPassed(long next) {
			long diff = next - current;
			if(diff >= 1000) {
				this.current = next;
				return true;
			}
			return false;
		}
//	}
}
