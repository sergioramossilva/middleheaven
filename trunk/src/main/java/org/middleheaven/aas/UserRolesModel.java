package org.middleheaven.aas;

import java.util.Set;

public interface UserRolesModel {

	
	public Set<Role> getUserRoles(User user);
	public boolean isUserInRole(User user, String role);
	
	 
}
