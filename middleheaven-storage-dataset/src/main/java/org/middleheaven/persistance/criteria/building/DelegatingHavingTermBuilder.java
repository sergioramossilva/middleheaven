/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public class DelegatingHavingTermBuilder implements HavingTermBuilder {

	private DataSetCriteriaBuilder builder;

	/**
	 * Constructor.
	 * @param dataSetCriteriaBuilder
	 */
	public DelegatingHavingTermBuilder(
			DataSetCriteriaBuilder builder) {
		this.builder = builder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HavingTermBuilder and() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HavingTermBuilder or() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HavingTermBuilder endExpectation() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HavingTermBuilder withExpectation() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> HavingColumnBuilder column(TypeDefinition<T> definition) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultColumnSetBuilder endExpectations() {
		return builder;
	}

}
