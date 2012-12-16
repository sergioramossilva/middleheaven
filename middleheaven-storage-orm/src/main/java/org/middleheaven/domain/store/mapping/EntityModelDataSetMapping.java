/**
 * 
 */
package org.middleheaven.domain.store.mapping;

import java.util.Collection;

import org.middleheaven.domain.store.EntityInstance;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.DataStoreSchemaName;

/**
 * 
 */
public interface EntityModelDataSetMapping {


	/**
	 * @return
	 */
	DataStoreSchemaName getSchemaName();
	
	/**
	 * @return
	 */
	String getDataSetName();

	/**
	 * @return
	 */
	boolean isSingleDataSetInheritance();

	/**
	 * @param currentRow
	 * @return
	 */
	Object read(DataRow currentRow);
	
	/**
	 * 
	 * Disassembles a entity instance in its composing datarows
	 * 
	 * @param currentRow
	 * @return
	 */
	Collection<DataRow> write(EntityInstance instance);

	/**
	 * @return
	 */
	EntityInstanceTypeMapper getTypeMapper();

	/**
	 * @param fieldName
	 */
	EntityInstanceTypeMapper getInstanceTypeMapper(String name);



}
