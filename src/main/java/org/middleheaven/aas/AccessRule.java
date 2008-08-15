package org.middleheaven.aas;

public interface AccessRule {

	/**
	 * Asserts that the given user has the given permission. If so, the method 
	 * returns quietly, otherwise an <code>AccessException</code> is thrown.
	 * 
	 * @param user the user who's permissions are being tested
	 * @param permission the permission the user must have
	 * @throws AccessException exception thrown if the user does not have the exception
	 */
	public void assertPermission(User user, Permission permission) throws AccessException;
}
