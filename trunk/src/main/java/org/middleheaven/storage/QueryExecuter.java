package org.middleheaven.storage;

import java.util.Collection;

public interface QueryExecuter {

	
	public <T> Collection<T> execute(ExecutableQuery<T> query);
}
