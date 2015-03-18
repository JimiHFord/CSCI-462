
public class KeyHelper {

	public static byte[] combineARK3KeyAndNonce(byte[] key, byte[] nonce) {
		byte[] arkKey = new byte[key.length + nonce.length];
		System.arraycopy(key, 0, arkKey, 0, key.length);
		System.arraycopy(nonce, 0, arkKey, key.length, nonce.length);
		return arkKey;
	}
}
