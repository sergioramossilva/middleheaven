package org.middleheaven.aas;

import java.util.Set;

/**
 * Attribute roles ( and thus permissions) based on the given credentials.
 */
public interface AutorizationModule {

	/**
	 * Attribute roles based on the given credentials.
	 * @param credentials
	 * @param roles
	 */
	public void autorize(Set<Credential> credentials , Set<Role> roles);
}
