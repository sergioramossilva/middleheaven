/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public interface OrderingBuilder {

	
	/**
	 * @param name
	 * @return
	 */
	DirectionOrderColumnSetBuilder by(TypeDefinition<String> name);

	/**
	 * @param aliasColumn
	 * @return
	 */
	DirectionOrderColumnSetBuilder by(String aliasColumn);

	
	ResultColumnSetBuilder endOrdering();

}
