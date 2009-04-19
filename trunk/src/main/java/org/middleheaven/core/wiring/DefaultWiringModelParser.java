package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.wiring.annotations.BindingSpecification;
import org.middleheaven.core.wiring.annotations.ScopeSpecification;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.util.classification.BooleanClassifier;

public class DefaultWiringModelParser extends AbstractAnnotationBasedWiringModelParser{

	@Override
	public <T> void parse(Class<T> type, WiringModel model) {
		
		if (model.getConstructorPoint()==null){

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

			Constructor<T> constructor  = constructors.get(0);

			WiringSpecification[] params = readParamsSpecification(constructor, new BooleanClassifier<Annotation>(){

				@Override
				public Boolean classify(Annotation a) {
					return a.annotationType().isAnnotationPresent(BindingSpecification.class) ||
					a.annotationType().isAnnotationPresent(ScopeSpecification.class);
				}
			
			});

			model.setConstructorPoint(new ConstructorWiringPoint(constructor,null,params));
		}
		
		// injection points

		Set<Field> fields = ReflectionUtils.allAnnotatedFields(type, Wire.class);

		for (Field f : fields){
			Set<Annotation> specs = ReflectionUtils.getAnnotations(f, BindingSpecification.class);
	
			model.addAfterWiringPoint(new FieldWiringPoint(f,WiringSpecification.search(f.getType(), specs)));
		}

		Set<Method> methods = ReflectionUtils.allAnnotatedMethods(type, Wire.class);

		for (Method method : methods){
			
			
			WiringSpecification[] params = readParamsSpecification(method, new BooleanClassifier<Annotation>(){

				@Override
				public Boolean classify(Annotation a) {
					return a.annotationType().isAnnotationPresent(BindingSpecification.class) ||
					a.annotationType().isAnnotationPresent(ScopeSpecification.class);
				}
			
			});
			
			// TODO read method specification
			model.addAfterWiringPoint(new MethodWiringPoint(method, null, params));
		}


	}

}
