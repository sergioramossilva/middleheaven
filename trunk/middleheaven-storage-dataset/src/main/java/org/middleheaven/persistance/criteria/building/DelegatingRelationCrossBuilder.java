package org.middleheaven.persistance.criteria.building;

/**
 * 
 */
class DelegatingRelationCrossBuilder implements
		RelationCrossBuilder {
	
	private DataSetCriteriaBuilder dataSetCriteriaBuilder;

	/**
	 * Constructor.
	 * @param dataSetCriteriaBuilder
	 */
	public DelegatingRelationCrossBuilder(DataSetCriteriaBuilder dataSetCriteriaBuilder) {
		this.dataSetCriteriaBuilder = dataSetCriteriaBuilder;
	}

	@Override
	public RelationsBuilder inner() {
		return new DelegatingRelationsBuilder(dataSetCriteriaBuilder, DelegatingRelationCrossBuilder.this , RelationOperator.INNER_JOIN);
	}

	@Override
	public OutterRelationsBuilder outter() {
		return new OutterRelationsBuilder(){

			@Override
			public RelationsBuilder left() {
				return new DelegatingRelationsBuilder(dataSetCriteriaBuilder,DelegatingRelationCrossBuilder.this, RelationOperator.OUTTER_LEFT_JOIN);
			}

			@Override
			public RelationsBuilder right() {
				return new DelegatingRelationsBuilder(dataSetCriteriaBuilder,DelegatingRelationCrossBuilder.this, RelationOperator.OUTTER_RIGHT_JOIN);
			}
			
		};
	}
}