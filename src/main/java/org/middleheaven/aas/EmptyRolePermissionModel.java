package org.middleheaven.aas;

public class EmptyRolePermissionModel implements RolesPermissionModel{

	@Override
	public PermissionSet getRolePermissions(Role role) {
		return new PermissionSet();
	}

}
