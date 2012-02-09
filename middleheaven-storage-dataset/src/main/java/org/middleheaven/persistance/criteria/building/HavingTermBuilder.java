/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public interface HavingTermBuilder {

	
	/**
	 * @return
	 */
	HavingTermBuilder and();

	
	/**
	 * @return
	 */
	HavingTermBuilder or();


	/**
	 * @return
	 */
	HavingTermBuilder endExpectation();

	/**
	 * @return
	 */
	HavingTermBuilder withExpectation();
	
	/**
	 * @param value
	 * @return
	 */
	<T> HavingColumnBuilder column(TypeDefinition<T> definition);

	
	ResultColumnSetBuilder endExpectations();

}
