package org.middleheaven.storage.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.data.DataType;
import org.middleheaven.storage.DefaultStorableFieldModel;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;

public class AnnotationsStorableEntityModel implements StorableEntityModel {

	Class<?> type;

	Map<QualifiedName,StorableFieldModel> fields = new HashMap<QualifiedName,StorableFieldModel>();
	String hardname;
	StorableFieldModel keyModel;
	
	public AnnotationsStorableEntityModel(Class<?> type){
		this.type = type;

		Table annot = ReflectionUtils.getAnnotation(type, Table.class);
		if (annot==null){
			throw new IllegalArgumentException("Annotation @Table missing");
		}

		hardname = annot.name();
		String key = annot.key();
		QualifiedName keyName = QualifiedName.of(hardname , key);
		
		keyModel = new DefaultStorableFieldModel(keyName, DataType.INTEGER).setKey(true);

		fields.put(keyName, keyModel);
		
		Set<Field> typeFields = ReflectionUtils.getAllFields(type);

		for (Field f : typeFields){
			boolean persistable = !Modifier.isTransient(f.getModifiers());
			String fieldName = f.getName();
			DataType dataType =  DataType.fromClass(f.getType()); 
			Column columnNote = ReflectionUtils.getAnnotation(f, Column.class);
			if (columnNote != null){
				persistable = persistable && columnNote.persistable();
				fieldName = columnNote.name().isEmpty() ? fieldName : columnNote.name();
				if (!columnNote.type().equals(DataType.UNKWON)){
					dataType = columnNote.type();
				}
			}
			if (!persistable){
				continue;
			}
			QualifiedName name = QualifiedName.of(hardname , fieldName);
			 
			DefaultStorableFieldModel d = new DefaultStorableFieldModel(name,dataType);
			fields.put(name, d);

		}
	}

	@Override
	public StorableFieldModel fieldModel(QualifiedName logicName) {
		// TODO adapt to get hardname from loginame
		return fields.get(logicName);
	}

	@Override
	public String hardNameForEntity() {
		return hardname;
	}


	@Override
	public StorableFieldModel keyFieldModel() {

		return keyModel;

	}

	public Collection<StorableFieldModel> fields() {
		return Collections.unmodifiableCollection(this.fields.values());
	}

	@Override
	public <T> T instanceFor(Class<T> type) {
		return ReflectionUtils.newInstance(type);
	}

}
