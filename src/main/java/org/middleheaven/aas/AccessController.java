package org.middleheaven.aas;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class AccessController implements AccessControlService {

	private Set<AccessRule> rules = new CopyOnWriteArraySet<AccessRule>();
	private AccessModel model;
	
	public AccessController(){}
	
	/**
	 * Asserts that the given user has the given permission. If so, the method 
	 * returns quietly, otherwise an <code>AccessException</code> is thrown.
	 * 
	 * @param user the user who's permissions are being tested
	 * @param permission the permission the user must have
	 * @throws AccessException exception thrown if the user does not have the exception
	 */
	public final void assertPermission(User user, Permission permission) throws AccessException{
		
		if (!user.isIdentified()){
			throw new UserNotIdentifiedAccessException();
		}
		
		for (AccessRule rule: rules){
			rule.assertPermission(model,user, permission);
		}
	}
	
	
	public void setAccessModel(AccessModel model){
		this.model = model;
	}
	
	public AccessController addRule(AccessRule rule){
		this.rules.add(rule);
		return this;
	}
	
	public AccessController removeRule(AccessRule rule){
		this.rules.remove(rule);
		return this;
	}

	@Override
	public void assertRole(User user, String roleName) throws RoleNotFoundException {
		if (!this.model.getUserRolesModel().isUserInRole(user, roleName)){
			throw new RoleNotFoundException(roleName);
		}
	}

	@Override
	public Set<Role> getUserRoles(User user) {
		return this.model.getUserRolesModel().getUserRoles(user);
	}
}
