package org.middleheaven.global.text.writeout;

/**
 * Indicates the number cannot be written out by the format by limitations
 * of the format object it self, namely it may lack the written equivalent for a number
 * to big or to small.
 */
public class NumberOutOfRangeException extends NumberWriteoutException {

	public NumberOutOfRangeException() {
		super("Number is cannot be writen out by this format");
	}

}
