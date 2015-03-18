
public class ARK3CFB {
	
	private final ARK3 encryptor = new ARK3();
	private final byte[] keystream = new byte [8];
	private int k;

	/**
	 * Returns this stream cipher's key size in bytes. If the stream cipher
	 * includes both a key and a nonce, <TT>keySize()</TT> returns the size of
	 * the key plus the nonce in bytes.
	 *
	 * @return  Key size.
	 */
	public int keySize() {
		return 24; // 16 key bytes, 8 nonce bytes
	}

	/**
	 * Set the key for this stream cipher. <TT>key</TT> must be an array of
	 * bytes whose length is equal to <TT>keySize()</TT>. If the stream cipher
	 * includes both a key and a nonce, <TT>key</TT> contains the bytes of the
	 * key followed by the bytes of the nonce. The keystream generator is
	 * initialized, such that successive calls to <TT>encrypt()</TT> will
	 * encrypt or decrypt a series of bytes.
	 * <P>
	 * For ARK3/CFB, bytes <TT>key[0]</TT> through <TT>key[17]</TT> are used.
	 *
	 * @param  key  Key.
	 */
	public void setKey (byte[] key) {
		encryptor.setKey (key);
		System.arraycopy (key, 16, keystream, 0, 8); // copy nonce in
		k = keystream.length;
	}

	/**
	 * Encrypt the given byte. Only the least significant 8 bits of
	 * <TT>b</TT> are used. <TT>b</TT> is a plaintext byte and the ciphertext
	 * byte is returned as a value from 0 to 255. 
	 *
	 * @param  b  Plaintext byte 
	 *
	 * @return  Ciphertext byte 
	 */
	public int encrypt(int b) {
		if (k == keystream.length) {
			encryptor.encrypt (keystream);
			k = 0;
		}
		keystream[k] = (byte) (b ^ (keystream[k] & 0xff) & 0xff);
		return keystream[k++];
	}
	
	/**
	 * Decrypt the given byte. Only the least significant 8 bits of
	 * <TT>b</TT> are used. <TT>b</TT> is a ciphertext byte and the plaintext
	 * byte is returned as a value from 0 to 255. 
	 *
	 * @param  b  Ciphertext byte 
	 *
	 * @return  Plaintext byte 
	 */
	public int decrypt(int b) {
		if(k == keystream.length) {
			encryptor.encrypt(keystream);
			k = 0;
		}
		byte nextKeystreamByte = (byte) (b & 0xff);
		b ^= (keystream[k] & 0xff);
		keystream[k++] = nextKeystreamByte;
		return b;
	}
}
