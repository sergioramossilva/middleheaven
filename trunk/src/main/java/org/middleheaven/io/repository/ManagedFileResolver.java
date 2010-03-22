package org.middleheaven.io.repository;

import java.net.URI;

public interface ManagedFileResolver {

	
	public ManagedFile resolveURI (URI filepath) throws UnsupportedSchemeException;
}
