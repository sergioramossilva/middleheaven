/**
 * 
 */
package org.middleheaven.storage.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.middleheaven.core.reflection.metaclass.MetaBean;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.domain.store.EntityInstance;
import org.middleheaven.domain.store.EntityInstanceField;
import org.middleheaven.domain.store.MetaBeanEntityInstance;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class EntityInstanceTypeMapper implements TypeMapper {

	private Map<QualifiedName, EntityFieldTypeMapper> fieldTypes = new HashMap<QualifiedName, EntityFieldTypeMapper>();
	private EntityModel model;
	private Set<DataColumnModel> columns;
	
	/**
	 * Constructor.
	 * @param name
	 */
	public EntityInstanceTypeMapper(EntityModel model) {
		this.model = model;
	}

	
	public synchronized Collection<DataColumnModel> getColumns(){
		if (columns != null){
			return columns;
		}
		
		Set<DataColumnModel> cs = new HashSet<DataColumnModel>();
		for (EntityFieldTypeMapper f : fieldTypes.values()){
			cs.addAll(Arrays.asList(f.getColumns()));
		}
		
		this.columns = cs;
		return this.columns;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMappedClassName() {
		return model.getEntityClass().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow row, Object aggregateParent, DataColumnModel... columns) {
	
		MetaBean bean = model.getEntityClass().newInstance();
		
		EntityInstance instance = new MetaBeanEntityInstance(model, bean);
		
		for (EntityInstanceField f : instance.getFields()){
			EntityFieldTypeMapper mapper = this.fieldTypes.get(f.getModel().getName());

			f.setValue(mapper.read(row,aggregateParent, columns));
		}
		
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object object, DataRow row, DataColumnModel... columns) {
		
		EntityInstance instance = (EntityInstance) object;
		
		for (EntityInstanceField f : instance.getFields()){
			EntityFieldTypeMapper mapper = this.fieldTypes.get(f.getModel().getName());

			mapper.write(f.getValue(), row, columns);
		}
		
	}

	/**
	 * @param fieldTypeMapper
	 */
	public void addFielTypeMapper(EntityFieldTypeMapper fieldTypeMapper) {
		fieldTypes.put(fieldTypeMapper.getFieldName(), fieldTypeMapper);
	}
	
	public EntityFieldTypeMapper getEntityFieldTypeMapper(QualifiedName fieldName){
		return fieldTypes.get(fieldName);
	}

}
