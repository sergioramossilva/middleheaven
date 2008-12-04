package org.middleheaven.aas;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * The access is granted on a role basis. 
 * Roles are matched to permissions explicitly using the <code>addPermission</code> method.
 *
 */
public class RolePresenceRule implements AccessRule {

	Map<String, PermissionSet > permissions = new TreeMap<String, PermissionSet >();
	
	@Override
	public final void assertPermission(AccessModel model, User user, Permission permission) throws AccessException{
		
		Set<Role> roles = model.getUserRolesModel().getUserRoles(user);
		
		for (Role role : roles){
			
			if (permissions.get(role.getName()).implies(permission)){
				return;
			}
			
		}
		throw new RoleNotFoundException();
	}

	
	public RolePresenceRule addPermission (String roleName, Permission ... permission){
		PermissionSet set = permissions.get(roleName);
		if (set==null){
			set = new PermissionSet();
			permissions.put(roleName,set);
		}
	   
	    for (Permission p : permission){
	    	set.add((ResourcePermission)p);  
	    }

		return this;
	}

}
