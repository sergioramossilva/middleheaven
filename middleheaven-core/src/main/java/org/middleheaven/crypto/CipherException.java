package org.middleheaven.crypto;

/**
 * Layer exception for any cipher algorithm problems.
 */
public class CipherException extends RuntimeException {

	private static final long serialVersionUID = 4325742842147056445L;

	/**
	 * 
	 * Constructor.
	 * @param e the cause exception
	 */
	public CipherException(Throwable e) {
		super(e);
	}

}
