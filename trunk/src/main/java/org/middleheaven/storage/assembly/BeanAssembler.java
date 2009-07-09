package org.middleheaven.storage.assembly;

import org.middleheaven.core.reflection.ClassIntrospector;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.PropertyAccessor;

public class BeanAssembler implements Assembler {

	@Override
	public Class<?> getAssembleType() {
		return null;
	}

	@Override
	public void assemble(AssemblyLineService service, AssemblyContext context, Data data) {
		Object object = data.getValue();

		if (object==null){
			object = Introspector.of(data.getValueType()).newInstance();
			data.setValue(object);
		}

		Iterable<PropertyAccessor> acessors = Introspector.of(data.getValueType()).inspect().properties().retriveAll();
		

		String typeName = data.getValueType().getName();
		restart: while (!context.isEmpty()){
			for (Data d : context){

				String fieldName = d.getName().substring(d.getName().indexOf(typeName) + typeName.length() + 1);
				ClassIntrospector<?> introspector = Introspector.of(data.getValueType());
				 
				if (fieldName.indexOf(".") >=0) {
					// composite
					String compositeFieldName = fieldName.substring(0,fieldName.indexOf("."));
					
					PropertyAccessor acessor = introspector
					.inspect().properties().named(compositeFieldName)
					.retrive();
					
					Class<?> fieldType = acessor.getValueType();
					Data subData = new BeanData(typeName + "." + compositeFieldName, null, fieldType);

					service.getAssembler(fieldType).assemble(service, context, subData);

					acessor.setValue(object, subData.getValue());
					continue restart;
				} else {

					
					introspector
					.inspect().properties().named(fieldName)
					.retrive().setValue(object,d.getValue());
					context.remove(d);
				}
			}
		}

	}

	@Override
	public void unAssemble(AssemblyLineService service, AssemblyContext context, Data data) {
		Object object = data.getValue();

		Iterable<PropertyAccessor> acessors = Introspector.of(object.getClass()).inspect().properties().retriveAll();

		for (PropertyAccessor acessor: acessors){
			Object propertyValue = acessor.getValue(object);

			context.put(new BeanData(
					data.getValueType().getName() + "." + acessor.getName() ,
					propertyValue,
					acessor.getValueType()
			));

		}

	}

}
