package org.middleheaven.aas;


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
