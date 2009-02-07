package org.middleheaven.storage.assembly;

import java.util.Collection;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionUtils;

public class BeanAssembler implements Assembler {

	@Override
	public void assemble(DataContext context, AssemblyLine line) {
		Object object = line.getObject();
		
		Collection<PropertyAccessor> acessors = ReflectionUtils.getPropertyAccessors(object.getClass());
		
		for (PropertyAccessor acessor: acessors){
			Object propertyValue = context.get(object.getClass() + "." + acessor.getName());
			
			if (line.isAssembled(propertyValue)){
				
				acessor.setValue(object, propertyValue);
			} else {
				
				acessor.setValue(object, line.assemble(context,propertyValue,object.getClass() + "." + acessor.getName()));

			}
		}
	}

	@Override
	public void unAssemble(DataContext context, AssemblyLine line) {
		Object object = line.getObject();
		
		Collection<PropertyAccessor> acessors = ReflectionUtils.getPropertyAccessors(object.getClass());
		
		for (PropertyAccessor acessor: acessors){
			Object propertyValue = acessor.getValue(object);
			
			if (!line.isDisassemblable(propertyValue)){
				context.put(
						line.getNameFor(line.getName() + "." + acessor.getName()) ,
						propertyValue
				);
			} else {
				
				line.unAssemble(context,propertyValue,line.getName() + "." + acessor.getName());

			}
		}
		
	}

	@Override
	public Class<?> getAssembleType() {
		return null;
	}

}
