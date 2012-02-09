/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public interface LogicRelationBuilder {

	/**
	 * @param id
	 * @return
	 */
	public <Type> RelationComparisonOperatorBuilder on(TypeDefinition<Type> column);

}
