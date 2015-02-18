
public class Generator {

	private int i = 0;
	private int j = 0;
	private final int KEY_LENGTH = KeyHelper.KEY_LENGTH;
	private final int[] key;
	
	public Generator(int[] key) {
		this.key = key.clone();
	}
	
	/*
	 * def gen_random_bytes(k):
		    """Yield a pseudo-random stream of bytes based on 256-byte array `k`."""
		    i = 0
		    j = 0
		    while True:
		        i = (i + 1) % 256
		        j = (j + k[i]) % 256
		        k[i], k[j] = k[j], k[i]
		        yield k[(k[i] + k[j]) % 256]
	 */
	public int next() {
		i = (i + 1) % KEY_LENGTH;
		j = (j + key[i]) % KEY_LENGTH;
		int temp = key[i];
		key[i] = key[j];
		key[j] = temp;
		return key[(key[i] + key[j]) % KEY_LENGTH];
	}
}
