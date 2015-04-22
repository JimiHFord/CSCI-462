import java.math.BigInteger;

/**
 * 
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-22-2015
 */
public class RsaGroupInput {
	/**
	 * A 2048-bit RSA public modulus.
	 */
	public final BigInteger publicModulus;
	
	/**
	 * An RSA public exponent.
	 */
	public final BigInteger publicExponent;
	
	/**
	 * An RSA ciphertext.
	 */
	public final BigInteger ciphertext;
	
	public RsaGroupInput(String publicModulus,
			String publicExponent, String ciphertext) {
		this.publicModulus = new BigInteger(publicModulus);
		this.publicExponent = new BigInteger(publicExponent);
		this.ciphertext = new BigInteger(ciphertext);
	}
}
