/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;


/**
 * 
 */
public interface RelationColumnsBuilder {


	/**
	 * @param id
	 * @return
	 */
	public <Type> RelationComparisonOperatorBuilder on(TypeDefinition<Type> column);

	/**
	 * @return
	 */
	public ResultColumnSetBuilder endRelations();

	/**
	 * @return
	 */
	public RelationCrossBuilder with();

}
