package org.middleheaven.model;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Enumerable;

/**
 * 
 * @param <M> de model type
 */
public abstract class AbstractEditableModelSet<M> implements ModelSet<M> {


	private final Map<String, M> models = new HashMap<String, M>();

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public <T extends M> Enumerable<T> models() {
		return  (Enumerable<T>) CollectionUtils.enhance(models.values());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public M getModelFor(Class<?> modelRefType) {
		return models.get(modelRefType.getName());
	}

	/**
	 * 
	 * @param <E> the modeled class type
	 * @param modelRefType the modeled class
	 * @param model the model for the class
	 */
	public <E> void addModel(Class<E> modelRefType, M model) {
		models.put(modelRefType.getName(), model);
	}
}
