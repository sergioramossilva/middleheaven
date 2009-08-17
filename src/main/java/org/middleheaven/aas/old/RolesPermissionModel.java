package org.middleheaven.aas.old;

import org.middleheaven.aas.PermissionSet;
import org.middleheaven.aas.Role;

public interface RolesPermissionModel {

	public PermissionSet getRolePermissions(Role role);
	
}
