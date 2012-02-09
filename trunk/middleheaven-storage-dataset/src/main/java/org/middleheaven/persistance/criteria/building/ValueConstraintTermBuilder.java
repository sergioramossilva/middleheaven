/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public interface ValueConstraintTermBuilder<TERMBUILDER> {

	
	/**
	 * @param i
	 * @return
	 */
	TERMBUILDER value(Object value);
	
	/**
	 * @param i
	 * @return
	 */
	TERMBUILDER parameter(String parameterName);
	
	/**
	 * @param date
	 * @return
	 */
	TERMBUILDER column(TypeDefinition<?> typeDef);

}
