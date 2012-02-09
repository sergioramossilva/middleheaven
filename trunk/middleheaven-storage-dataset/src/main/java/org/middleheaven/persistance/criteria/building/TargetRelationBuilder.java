/**
 * 
 */
package org.middleheaven.persistance.criteria.building;



/**
 * 
 */
public interface TargetRelationBuilder {

	/**
	 * @return
	 */
	RelationCrossBuilder with();

	
	public LogicRelationBuilder and();
	
	public LogicRelationBuilder or();
	

	/**
	 * @return
	 */
	DataSetCriteriaBuilder endRelations();

}
