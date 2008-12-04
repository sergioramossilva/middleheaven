package org.middleheaven.aas;

import java.util.Set;

public interface AccessControlService {

	
	
	public void assertPermission(User user, Permission permission) throws AccessException;

	
	public void assertRole(User user, String name) throws RoleNotFoundException;
	
	
	public Set<Role> getUserRoles(User user);
}
