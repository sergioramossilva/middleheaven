/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public interface ColumnSetBuilder {

	/**
	 * @param model
	 * @return
	 */
	<T> ColumnSetBuilder column(TypeDefinition<T> model);

	/**
	 * @return
	 */
	OrderColumnSetBuilder order();

}
