/**
 * 
 */
package org.middleheaven.domain.store.mapping;

import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.storage.types.TypeMapper;
import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public interface DomainModelDataSetTypeMapper {

	/**
	 * @param entityModel
	 * @return
	 */
	EntityModelDataSetMapping getMappingFor(EntityModel entityModel);

	public void registerTypeMapper(TypeMapper typeMapper);
	public void unregisterTypeMapper(TypeMapper typeMapper);
	
	/**
	 * @param fieldName
	 */
	EntityFieldTypeMapper getEntityFieldTypeMapper(QualifiedName fieldName);
}
