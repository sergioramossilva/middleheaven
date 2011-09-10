package org.middleheaven.aas;

import java.util.Set;

public interface SubjectLocator {

	
	public Subject load(Set<Credential> credentials , Set<Role> roles);
}
