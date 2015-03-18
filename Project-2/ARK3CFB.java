
public class ARK3CFB {
	
	private ARK3 encryptor = new ARK3();
	private byte[] keystream = new byte [8];
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
		k = 8;
	}

	/**
	 * Encrypt or decrypt the given byte. Only the least significant 8 bits of
	 * <TT>b</TT> are used. If <TT>b</TT> is a plaintext byte, the ciphertext
	 * byte is returned as a value from 0 to 255. If <TT>b</TT> is a ciphertext
	 * byte, the plaintext byte is returned as a value from 0 to 255.
	 *
	 * @param  b  Plaintext byte (if encrypting), ciphertext byte (if
	 *            decrypting).
	 *
	 * @return  Ciphertext byte (if encrypting), plaintext byte (if decrypting).
	 */
	public int encrypt(int b) {
		if (k == 8) {
			encryptor.encrypt (keystream);
			k = 0;
		}
		return b ^ (keystream[k++] & 0xff);
	}
}
