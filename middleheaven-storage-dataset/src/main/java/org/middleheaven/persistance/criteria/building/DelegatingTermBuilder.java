/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.model.TypeDefinition;
import org.middleheaven.util.classification.LogicOperator;

/**
 * 
 */
public class DelegatingTermBuilder implements RestrictionTermBuilder , LogicConstraintHolder  {

	
	private DataSetCriteriaBuilder builder;
	LogicConstraint logic;
	
	public DelegatingTermBuilder (DataSetCriteriaBuilder builder){
		this.builder = builder;
		logic = new LogicConstraint(LogicOperator.and());
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RestrictionTermBuilder and() {
		logic = new LogicConstraint(LogicOperator.and());
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RestrictionTermBuilder or() {
		logic = new LogicConstraint(LogicOperator.or());
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> ConditionTermComparisonOperatorBuilder column(TypeDefinition<?> definition) {
		return new DelegatingWhereTermComparisonOperatorBuilder(new ColumnNameValueLocator(definition.getQualifiedName()), builder, this, logic);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultColumnSetBuilder endTerms() {
		builder.criteria.addLogicConstraint(logic);
		return builder;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public RestrictionTermBuilder withTerm() {
		return new DelegatingTermBuilder(builder);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public RestrictionTermBuilder endTerm() {
		builder.criteria.addLogicConstraint(logic);
		return this;

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public LogicConstraint getLogicConstraint() {
		return logic;
	}

}
