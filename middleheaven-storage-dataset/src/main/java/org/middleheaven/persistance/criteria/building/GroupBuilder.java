/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public interface GroupBuilder {

	
	/**
	 * 
	 * @param model
	 * @return
	 */
	GroupBuilder column(TypeDefinition<?> typeDef);


	ResultColumnSetBuilder endGroup();
	
}
