package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.criteria.DataSetCriteria;
import org.middleheaven.persistance.model.TypeDefinition;


public final class DataSetCriteriaBuilder implements ResultColumnSetBuilder{

	
	/**
	 * 
	 * @return returns a new instance of a DataSetCriteriaBuilder.
	 */
	public static ResultColumnSetBuilder retrive(){
		return new DataSetCriteriaBuilder();
	}
	
	private DataSetCriteriaBuilder (){}
	
	protected DataSetCriteria criteria = new DataSetCriteria();
	
	
	
	/**
	 * 
	 * @return the DataSetCriteria that will return all found rows.
	 */
	public DataSetCriteria all(){
		return criteria;
	}
	
	/**
	 * 
	 * @return the DataSetCriteria that will return all found rows.
	 * @param maxCount the max quantity of rows to be retrieved.
	 */
	public DataSetCriteria limit(int maxCount){
		return limit(maxCount , 0);
	}
	
	
	/**
	 * 
	 * @return the DataSetCriteria that will return all found rows.
	 * @param maxCount the max quantity of rows to be retrieved.
	 * @param offSet the  quantity of rows to be skipped before start counting for max count.
	 */
	public DataSetCriteria limit(int maxCount, int offSet){
		criteria.setMaxCount(maxCount);
		criteria.setOffset(offSet);
		return criteria;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AliasColumnBuilder rowsCount() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConditionaRestrictionlBuilder restricted() {
		return new ConditionaRestrictionlBuilder(){

			@Override
			public RestrictionTermBuilder withTerm() {
				return new DelegatingTermBuilder(DataSetCriteriaBuilder.this);
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RelationBuilder related() {
		return new RelationBuilder(){

			@Override
			public RelationCrossBuilder with() {
				return new DelegatingRelationCrossBuilder(DataSetCriteriaBuilder.this);
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GrouppingBuilder grouped() {
		return new GrouppingBuilder(){

			@Override
			public GroupBuilder withGroup() {
				return new DelegatingGroupBuilder(DataSetCriteriaBuilder.this);
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderColumnSetBuilder ordered() {
		return new OrderColumnSetBuilder(){

			@Override
			public OrderingBuilder withOrdering() {
				return new DelegatingOrderingBuilder(DataSetCriteriaBuilder.this);
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HavingBuilder aggregated() {
		return new HavingBuilder(){

			@Override
			public HavingTermBuilder withExpectation() {
				return new DelegatingHavingTermBuilder(DataSetCriteriaBuilder.this);
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RetriveColumnsBuilder withColumns() {
		return new RetriveColumnsBuilder(){

			@Override
			public <T> ColumnBuilder column(TypeDefinition<T> definition) {
				return new DelegatingColumnBuilder(definition.getQualifiedName(), DataSetCriteriaBuilder.this) ;
			}

			@Override
			public ResultColumnSetBuilder endColumns() {
				return DataSetCriteriaBuilder.this;
			}
			
		};
	}



	
}
