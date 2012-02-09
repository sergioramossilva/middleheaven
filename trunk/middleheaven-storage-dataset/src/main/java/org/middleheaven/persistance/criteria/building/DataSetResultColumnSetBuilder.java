/**
 * 
 */
package org.middleheaven.persistance.criteria.building;


/**
 * 
 */
public interface DataSetResultColumnSetBuilder {

	/**
	 * 
	 * @param names
	 * @return
	 */
	ResultColumnSetBuilder column(String ... names);

	/**
	 * @param string
	 * @return
	 */
	FunctiontCriteriaBuilder sum(String column);

	/**
	 * @param string
	 * @return
	 */
	FunctiontCriteriaBuilder max(String column);

	/**
	 * @param string
	 * @return
	 */
	FunctiontCriteriaBuilder min(String column);
	
	/**
	 * @param string
	 * @return
	 */
	FunctiontCriteriaBuilder avg(String column);
	
	/**
	 * @param string
	 * @return
	 */
	FunctiontCriteriaBuilder count(String column);
	
	/**
	 * @param string
	 * @return
	 */
	FunctiontCriteriaBuilder rowsCount();
}
