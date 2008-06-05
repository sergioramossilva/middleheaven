package org.middleheaven.dependency;

import java.util.Set;

public interface DependencyResolver {

	
	/**
	 * Determine the <code>Dependency</code> on whom the passed object depends
	 * @param obj the test object
	 * @return a <code>Set</code> of Object on whom <code>obj</code> depends.
	 */
	public  Set<Dependency> resolveDependencies(Object obj);
}
