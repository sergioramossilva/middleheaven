package org.middleheaven.aas;

import java.util.Set;


/**
 * The access is granted on a role basis. 
 * Roles are matched to permissions by using <code>RolesPermissionModel</code> in the current <code>AuthenticationContext</code>
 *
 */
public class RolePermissionRule implements AccessRule {

	@Override
	public void assertPermission(AccessModel model, User user, Permission permission) throws AccessException {
		
		Set<Role> roles = model.getUserRolesModel().getUserRoles(user);

		RolesPermissionModel rolePermissionModel = model.getRolePermissionModel();
		
		for (Role role : roles){
			if (rolePermissionModel.getRolePermissions(role).implies(permission)){
				return;
			}
		}
		
		throw new AccessDeniedException();
	}

}
