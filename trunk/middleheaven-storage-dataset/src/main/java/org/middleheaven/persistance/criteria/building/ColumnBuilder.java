/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public interface ColumnBuilder {

	/**
	 * @return
	 */
	ColumnBuilder value();

	/**
	 * @param birthdate
	 * @return
	 */
	<T> ColumnBuilder column(TypeDefinition<?> definition);

	
	/**
	 * @return
	 */
	ColumnBuilder sum();

	/**
	 * @return
	 */
	ColumnBuilder max();

	/**
	 * @return
	 */
	ColumnBuilder min();
	
	/**
	 * @return
	 */
	ColumnBuilder avg();
	
	/**
	 * @return
	 */
	ColumnBuilder count();

	/**
	 * @param string
	 * @return
	 */
	ColumnBuilder as(String string);

	/**
	 * @return
	 */
	DataSetCriteriaBuilder endColumns();
}
