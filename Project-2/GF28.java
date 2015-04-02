//******************************************************************************
//
// File:    GF28.java
// Package: ---
// Unit:    Class GF28
//
//******************************************************************************

/**
 * Class contains the methods that perform GF 2^8 addition and multiplication
 * 
 * @author Jimi Ford (jhf3617)
 * @version 3-21-2015
 */
public class GF28 {
	
	/**
	 * Perform GF 2^8 addition
	 * @param a one polynomial to add
	 * @param b the other polynomial to add
	 * @return the two polynomials, <TT>a</TT> and <TT>b</TT>, added together
	 */
	public static int add(int a, int b) {
		return (a ^ b);
	}
	
	/**
	 * Perform GF 2^8 multiplication
	 * @param a one polynomial to add
	 * @param b the other polynomial to add
	 * @param IRREDUCIBLE the irreducible polynomial to use for multiplication
	 * @return the two polynomials, <TT>a</TT> and <TT>b</TT>, multiplied 
	 * together
	 */
	public static int multiply(int a, int b, final int IRREDUCIBLE) {
		final int p = IRREDUCIBLE;
		int result = 0;
		for (int bit = 0x80; bit > 0; bit >>= 1) {
		    result <<= 1;
		    if ((result & 0x100) != 0) {
		    	result ^= p;
		    }
		    if ((b & bit) != 0) {
		    	result ^= a;
		    }
	    }
		return result;
	}
}
