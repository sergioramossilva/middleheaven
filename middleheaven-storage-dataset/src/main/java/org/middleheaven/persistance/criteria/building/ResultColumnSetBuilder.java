/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.criteria.DataSetCriteria;

/**
 * 
 */
public interface ResultColumnSetBuilder {

	
	RetriveColumnsBuilder withColumns();
	

	/**
	 * 
	 * Sets the result to the count of all rows.
	 * @return
	 */
	AliasColumnBuilder rowsCount();
	
	/**
	 * @return
	 */
	ConditionaRestrictionlBuilder restricted();
	
	/**
	 * 
	 * @return the DataSetCriteria that will return all found rows.
	 */
	DataSetCriteria all();

	
	/**
	 * 
	 * @return the DataSetCriteria that will return all found rows.
	 * @param maxCount the max quantity of rows to be retrieved.
	 */
	public DataSetCriteria limit(int maxCount);
	
	/**
	 * 
	 * @return the DataSetCriteria that will return all found rows.
	 * @param maxCount the max quantity of rows to be retrieved.
	 * @param offSet the  quantity of rows to be skipped before start counting for max count.
	 */
	public DataSetCriteria limit(int maxCount, int offSet);


	/**
	 * @return
	 */
	RelationBuilder related();

	/**
	 * @return
	 */
	GrouppingBuilder grouped();

	/**
	 * @return
	 */
	OrderColumnSetBuilder ordered();

	/**
	 * @return
	 */
	HavingBuilder aggregated();


}
