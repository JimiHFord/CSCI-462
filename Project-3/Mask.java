//******************************************************************************
//
// File:    Mask.java
// Package: ---
// Unit:    Class Mask
//
//******************************************************************************

/**
 * This class provides utility methods to chop 48-bit (longs) into 28-bit (ints)
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-8-2015
 */
public class Mask {

	/**
	 * 48-bit mask
	 */
	public final static long MASK_48 = 0xffffffffffffL;
	
	/**
	 * 24-bit mask
	 */
	public final static int  MASK_24 = 0xffffff;
	
	/**
	 * chop the top 24 bits off of the given long and return them in an int
	 * @param in the given long
	 * @return the top 24 bits
	 */
	public static int chopHigh(long in) {
		return (int)(in >>> 24) & MASK_24;
	}
	
	/**
	 * chop the lower 24 bits off of the given long and return them in an int
	 * @param in the given long
	 * @return the lower 24 bits
	 */
	public static int chopLow(long in) {
		return (int)(in) & MASK_24;
	}
}
