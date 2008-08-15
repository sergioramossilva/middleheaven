package org.middleheaven.aas;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class AccessController {

	private Set<AccessRule> rules = new CopyOnWriteArraySet<AccessRule>();
	
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
			rule.assertPermission(user, permission);
		}
	}
	
	public AccessController addRule(AccessRule rule){
		this.rules.add(rule);
		return this;
	}
	
	public AccessController removeRule(AccessRule rule){
		this.rules.remove(rule);
		return this;
	}
}
