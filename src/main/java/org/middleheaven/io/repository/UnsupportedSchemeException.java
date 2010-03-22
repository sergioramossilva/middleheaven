package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;

public class UnsupportedSchemeException extends ManagedIOException {

	public UnsupportedSchemeException(String msg) {
		super(msg);
	}

}
