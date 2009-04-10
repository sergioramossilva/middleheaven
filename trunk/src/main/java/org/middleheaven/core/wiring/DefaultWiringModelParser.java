package org.middleheaven.core.wiring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionUtils;

public class DefaultWiringModelParser implements WiringModelParser {

	@Override
	public <T> void parse(Class<T> type, WiringModel model) {

		// constructor
		List<Constructor<T>> constructors =  ReflectionUtils.allAnnotatedConstructors( type, Wire.class);

		if (constructors.isEmpty()){
			// search not annotated constructors
			constructors = ReflectionUtils.constructors(type);
			if (constructors.size()>1){
				throw new ConfigurationException("Multiple constructors found for " + type + ". Annotate only one with @" + Wire.class.getSimpleName());
			}
		} else if (constructors.size()>1){
			throw new ConfigurationException("Only one constructor may be annotated with @" + Wire.class.getSimpleName());
		} 

		Constructor<T> selectedConstructor  = constructors.get(0);

		model.setConstructorPoint(new ConstructorWiringPoint(selectedConstructor));

		// injection points

		Set<Field> fields = ReflectionUtils.allAnnotatedFields(type, Wire.class);

		for (Field f : fields){
			model.addAfterWiringPoint(new FieldWiringPoint(f));
		}

		Set<Method> methods = ReflectionUtils.allAnnotatedMethods(type, Wire.class);

		for (Method m : methods){
			model.addAfterWiringPoint(new MethodWiringPoint(m));
		}


	}

}
