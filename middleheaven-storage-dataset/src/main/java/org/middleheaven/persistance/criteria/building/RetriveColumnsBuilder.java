/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public interface RetriveColumnsBuilder {

	
	/**
	 * 
	 * Adds a new column to the result.
	 * 
	 * @param definition the column definition
	 * 
	 * @return
	 */
	<T> ColumnBuilder column(TypeDefinition<T> definition);

	
	ResultColumnSetBuilder endColumns();
}
