package org.middleheaven.namedirectory;


public interface NameDirectoryService {

	public <T> T lookup(String name, Class<T> type) throws NamingDirectoryException;

}
