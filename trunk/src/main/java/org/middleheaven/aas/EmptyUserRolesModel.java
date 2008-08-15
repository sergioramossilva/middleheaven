package org.middleheaven.aas;

import java.util.Collections;
import java.util.Set;

public final class EmptyUserRolesModel implements UserRolesModel {

	@Override
	public Set<Role> getUserRoles(User user) {
		return Collections.emptySet();
	}

	@Override
	public boolean isUserInRole(User user, String role) {
		return false;
	}

}
