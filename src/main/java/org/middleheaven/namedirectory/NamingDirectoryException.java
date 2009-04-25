package org.middleheaven.namedirectory;

import javax.naming.NamingException;

public class NamingDirectoryException extends RuntimeException {

	public NamingDirectoryException(NamingException e) {
		super(e);
	}

}
