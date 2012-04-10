package org.middleheaven.namedirectory;

import org.middleheaven.core.wiring.service.Service;


/**
 * Abstract access to a name and directory service
 */
@Service
public interface NameDirectoryService {

	public <T> T lookup(String name, Class<T> type) throws NamingDirectoryException;

	public Iterable<NameTypeEntry> listTypes(String nameFilter) throws NamingDirectoryException;
	
	public Iterable<NameObjectEntry> listObjects(String nameFilter) throws NamingDirectoryException;
}
