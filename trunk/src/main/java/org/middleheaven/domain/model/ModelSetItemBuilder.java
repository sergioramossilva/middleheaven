package org.middleheaven.domain.model;

/**
 * 
 * @param <T>
 */
public interface ModelSetItemBuilder<T> {

	/**
	 * 
	 * @param type the entity class.
	 * @return the editable model for the given class.
	 */
	public T getEditableModelOf(Class<?> type);
}
