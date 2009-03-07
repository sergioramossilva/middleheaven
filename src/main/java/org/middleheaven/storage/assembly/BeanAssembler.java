package org.middleheaven.storage.assembly;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionUtils;

public class BeanAssembler implements Assembler {

	@Override
	public Class<?> getAssembleType() {
		return null;
	}

	@Override
	public void assemble(AssemblyLineService service, AssemblyContext context, Data data) {
		Object object = data.getValue();

		if (object==null){
			object = ReflectionUtils.newInstance(data.getValueType());
			data.setValue(object);
		}

		Iterable<PropertyAccessor> acessors = ReflectionUtils.getPropertyAccessors(data.getValueType());

		String typeName = data.getValueType().getName();
		restart: while (!context.isEmpty()){
			for (Data d : context){

				String fieldName = d.getName().substring(d.getName().indexOf(typeName) + typeName.length() + 1);

				if (fieldName.indexOf(".") >=0) {
					// composite
					String compositeFieldName = fieldName.substring(0,fieldName.indexOf("."));
					PropertyAccessor acessor = ReflectionUtils.getPropertyAccessor(data.getValueType(), compositeFieldName);

					Class<?> fieldType = acessor.getValueType();
					Data subData = new BeanData(typeName + "." + compositeFieldName, null, fieldType);

					service.getAssembler(fieldType).assemble(service, context, subData);

					acessor.setValue(object, subData.getValue());
					continue restart;
				} else {
					ReflectionUtils.getPropertyAccessor(data.getValueType(), fieldName).setValue(object,d.getValue());
					context.remove(d);
				}
			}
		}

	}

	@Override
	public void unAssemble(AssemblyLineService service, AssemblyContext context, Data data) {
		Object object = data.getValue();

		Iterable<PropertyAccessor> acessors = ReflectionUtils.getPropertyAccessors(object.getClass());

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
