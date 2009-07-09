package org.middleheaven.aas.old;

import org.middleheaven.aas.Role;


/**
 * Empty implementation of  RolesPermissionModel.
 * No role has permissions
 */
public class EmptyRolePermissionModel implements RolesPermissionModel{

	@Override
	public PermissionSet getRolePermissions(Role role) {
		return new PermissionSet();
	}

}
