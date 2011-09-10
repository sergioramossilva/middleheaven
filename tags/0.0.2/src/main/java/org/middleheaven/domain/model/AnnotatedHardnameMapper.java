package org.middleheaven.domain.model;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.domain.model.HardnameMapper;
import org.middleheaven.storage.model.annotations.Column;
import org.middleheaven.storage.model.annotations.Table;


public class AnnotatedHardnameMapper extends HardnameMapper {

	@Override
	public String getFieldHardname(Class<?> type,	String logicFieldName) {
		String hardName =null;
		PropertyAccessor pa = Introspector.of(type).inspect().properties().named(logicFieldName).retrive();

		if(pa.isAnnotadedWith(Column.class)){
			Column column = pa.getAnnotation(Column.class);
			String columnName = column.value();
			if (columnName.trim().isEmpty()){
				columnName = null;
			} else {
				hardName = columnName;
			}
		}
		
		if (hardName==null){
			hardName = this.mapFieldHardnameFromLogicname(logicFieldName);
		}
		
		
		return hardName;
	}

	@Override
	public String getEntityHardname(Class<?> type) {
		String hardEntityName=null;
		if (type.isAnnotationPresent(Table.class)){
			
			Table table = type.getAnnotation(Table.class);
			hardEntityName = table.name();
			if (hardEntityName.trim().isEmpty()){
				hardEntityName = null;
			}
		}
		
		if (hardEntityName==null){
			hardEntityName = this.mapEntityHardnameFromLogicname(type.getSimpleName());
		}

		return hardEntityName;
	}

}
