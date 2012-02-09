/**
 * 
 */
package org.middleheaven.domain.store.mapping;

import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.storage.types.EntityFieldTypeMapper;
import org.middleheaven.storage.types.TypeMapper;
import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public interface DomainModelDataSetMapper {

	/**
	 * @param entityModel
	 * @return
	 */
	EntityModelDataSetMapping getMappingFor(EntityModel entityModel);

	public void registerTypeMapper(TypeMapper typeMapper);
	public void unregisterTypeMapper(TypeMapper typeMapper);
	
	void initialize();

	/**
	 * @param fieldName
	 */
	EntityFieldTypeMapper getEntityFieldTypeMapper(QualifiedName fieldName);
}
