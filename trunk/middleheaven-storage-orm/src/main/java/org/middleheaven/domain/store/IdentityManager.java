package org.middleheaven.domain.store;


/**
 * Provide Identity generation and assignement.
 */
public interface IdentityManager {

	
	/**
	 * Assign identity token for the object , if it has none
	 * @return EntityInstance after assigning identity
	 */
	public EntityInstance assignIdentity(EntityInstance storable);
	
	
}
