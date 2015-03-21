//******************************************************************************
//
// File:    GF28.java
// Package: ---
// Unit:    Class GF28
//
//******************************************************************************


public class GF28 {

	
	
	public static int add(int a, int b) {
		return (a ^ b);
	}
	
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
