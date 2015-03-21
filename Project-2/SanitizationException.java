//******************************************************************************
//
// File:    SanitizationException.java
// Package: ---
// Unit:    Class SanitizationException
//
//******************************************************************************

@SuppressWarnings("serial")
/**
 * This exception is thrown when the <TT>Sanitize</TT> class encounters an
 * error with the input at runtime.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 3-21-2015
 */
public class SanitizationException extends Exception {

	/**
	 * Construct a SanitizationException
	 * @param msg the error message describing the problem
	 */
	public SanitizationException(String msg) {
		super(msg);
	}
}
