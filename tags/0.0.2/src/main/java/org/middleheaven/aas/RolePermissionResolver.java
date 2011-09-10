package org.middleheaven.aas;

/**
 * Simple {@code PermissionResolver} based on roles. 
 * Validates if the permission is a {@code RolePermission} and the subject has
 * that role. Otherwise negates permission.
 */
public class RolePermissionResolver implements PermissionResolver {

	@Override
	public boolean hasPermission(Subject s, Permission p) {
		if (p instanceof RolePermission) {
			return s.isInRole(((RolePermission)p).getRoleName());
		}
		return false;
	}

}
