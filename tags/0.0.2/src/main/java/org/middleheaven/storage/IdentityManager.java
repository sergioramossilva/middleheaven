package org.middleheaven.storage;

import org.middleheaven.util.identity.Identity;

public interface IdentityManager {

	
	/**
	 * Determine the object's identity token.
	 * @param object
	 * @return
	 */
	public Identity getIdentityFor(Object object);
	
	/**
	 * Assign identity token for the object , if it has none
	 * @return storable after assigning identity
	 */
	public Storable assignIdentity(Storable storable);
	
	
}
