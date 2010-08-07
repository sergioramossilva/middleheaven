package org.middleheaven.domain;

import org.middleheaven.util.collections.Enumerable;

/**
 * Holds the individual {@code EntityModel}s for all entities in the domain.
 * 
 */
public interface DomainModel {

	/**
	 * 
	 * @param <T> the EntityModel type
	 * @return and Enumerable  with all the EntityModel of the domain.
	 */
	public <T extends EntityModel> Enumerable<T> entitiesModels();	
	
	/**
	 * 
	 * @param entityType the entity class
	 * @return the class's corresponding EntityModel.
	 */
	public EntityModel getEntityModelFor(Class<?> entityType);

	
}
