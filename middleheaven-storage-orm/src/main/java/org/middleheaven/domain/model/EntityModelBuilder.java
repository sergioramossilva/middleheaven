package org.middleheaven.domain.model;


/**
 * Model builder for entities.
 * 
 */
public interface EntityModelBuilder extends ModelSetItemBuilder<EditableDomainEntityModel> {

	/**
	 * 
	 * {@inheritDoc}
	 */
	public EditableDomainEntityModel getEditableModelOf(Class<?> type);
	

}
