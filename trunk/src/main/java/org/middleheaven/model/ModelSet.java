package org.middleheaven.model;

import org.middleheaven.util.collections.Enumerable;

/**
 * The set of models, maped by modeled classs.
 * @param <M>
 */
public interface ModelSet<M> {

	
	/**
	 * 
	 * @param <T> the model type
	 * @return and Enumerable  with all the EntityModel of the domain.
	 */
	public <T extends M> Enumerable<T> models();	

	/**
	 * 
	 * @param modelRefType the modeled class 
	 * @return the class's corresponding model.
	 */
	public M getModelFor(Class<?> modelRefType);
}
