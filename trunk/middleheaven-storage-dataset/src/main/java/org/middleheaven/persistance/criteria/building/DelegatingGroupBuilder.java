/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public class DelegatingGroupBuilder implements GroupBuilder {

	private DataSetCriteriaBuilder builder;

	/**
	 * Constructor.
	 * @param dataSetCriteriaBuilder
	 */
	public DelegatingGroupBuilder(DataSetCriteriaBuilder dataSetCriteriaBuilder) {
		this.builder = dataSetCriteriaBuilder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupBuilder column(TypeDefinition<?> typeDef) {
		builder.criteria.addGroupConstraint(new ColumnGroupConstraint(typeDef.getQualifiedName()));
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultColumnSetBuilder endGroup() {
		return builder;
	}

}
