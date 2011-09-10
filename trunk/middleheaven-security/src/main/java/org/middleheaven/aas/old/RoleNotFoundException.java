package org.middleheaven.aas.old;

import org.middleheaven.aas.AccessException;

public class RoleNotFoundException extends AccessException {


	private static final long serialVersionUID = 8953145786163335437L;
	
	private String roleName = null;
	
	public RoleNotFoundException(){
		super("User not in role");
	}

	public RoleNotFoundException(String roleName) {
		super("User not in role " + roleName);
		this.roleName = roleName;
	}

	
	public String getRoleName(){
		return roleName;
	}
}
