/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public class DelegatingOrderingBuilder implements OrderingBuilder {

	private DataSetCriteriaBuilder builder;

	/**
	 * Constructor.
	 * @param dataSetCriteriaBuilder
	 */
	public DelegatingOrderingBuilder(
			DataSetCriteriaBuilder dataSetCriteriaBuilder) {
		this.builder = dataSetCriteriaBuilder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DirectionOrderColumnSetBuilder by(final TypeDefinition<String> name) {
		return new DirectionOrderColumnSetBuilder(){

			@Override
			public OrderingBuilder asc() {
				builder.criteria.addOrderingConstraint(new ColumnOrderConstraint(name.getQualifiedName(), true));
				return DelegatingOrderingBuilder.this;
			}

			@Override
			public OrderingBuilder desc() {
				builder.criteria.addOrderingConstraint(new ColumnOrderConstraint(name.getQualifiedName(), false));
				return DelegatingOrderingBuilder.this;
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DirectionOrderColumnSetBuilder by(final String aliasColumn) {
		return new DirectionOrderColumnSetBuilder(){

			@Override
			public OrderingBuilder asc() {
				builder.criteria.addOrderingConstraint(new AliasOrderConstraint(aliasColumn, true));
				return DelegatingOrderingBuilder.this;
			}

			@Override
			public OrderingBuilder desc() {
				builder.criteria.addOrderingConstraint(new AliasOrderConstraint(aliasColumn, false));
				return DelegatingOrderingBuilder.this;
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultColumnSetBuilder endOrdering() {
		return builder;
	}

}
