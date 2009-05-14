package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.wiring.annotations.BindingSpecification;
import org.middleheaven.core.wiring.annotations.Name;
import org.middleheaven.core.wiring.annotations.Params;
import org.middleheaven.core.wiring.annotations.ScopeSpecification;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.CollectionUtils;

public class DefaultWiringModelParser extends AbstractAnnotationBasedWiringModelParser{


	private Class<? extends Annotation>[] annotations;

	public DefaultWiringModelParser(){
		this(Wire.class);
	}

	public DefaultWiringModelParser(Class<? extends Annotation> ... adicionalAnnotations){
		this.annotations = CollectionUtils.addToArray(adicionalAnnotations, Wire.class);
	}

	@Override
	public <T> void readWiringModel(Class<T> type, WiringModel model) {
		// TODO implement WiringModelReader.readWiringModel

		if (model.getConstructorPoint()==null){

			// constructor
			List<Constructor<T>> constructors =  ReflectionUtils.allAnnotatedConstructors( type, annotations);

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

		Set<Field> fields = ReflectionUtils.allAnnotatedFields(type, annotations);

		for (Field f : fields){
			Set<Annotation> specs = ReflectionUtils.getAnnotations(f, Wire.class);

			WiringSpecification<?> spec = WiringSpecification.search(f.getType(), specs);
			
			Wire wire = ReflectionUtils.getAnnotation(f, Wire.class);
			
			spec.setRequired(wire.required());
			spec.setShareable(wire.shareabled());
			
			model.addAfterWiringPoint(new FieldWiringPoint(f,spec));
		}

		Set<Method> methods = ReflectionUtils.allAnnotatedMethods(type, annotations);

		for (Method method : methods){

			
			WiringSpecification[] params = readParamsSpecification(method, new BooleanClassifier<Annotation>(){

				@Override
				public Boolean classify(Annotation a) {
					return a.annotationType().isAnnotationPresent(Wire.class) ||
					a.annotationType().isAnnotationPresent(ScopeSpecification.class);
				}

			});

			// TODO read method specification
			model.addAfterWiringPoint(new MethodWiringPoint(method, null, params));
		}


	}

	@Override
	public void readScoopingModel(Object obj, ScoopingModel model) {
		Set<Annotation> annotations = ReflectionUtils.getAnnotations(obj.getClass());
		
		for (Annotation a: annotations){
			if (a.annotationType().isAnnotationPresent(ScopeSpecification.class)){
				model.addScope(a.annotationType());
			}else if (a.annotationType().isAnnotationPresent(Name.class)){
				Name name = a.annotationType().getAnnotation(Name.class);
				model.addParam("name", name.value());
			} else if (a.annotationType().isAnnotationPresent(Params.class)){
				Params name = a.annotationType().getAnnotation(Params.class);
				String[] paramPairs = name.value();
				for (String paramPair : paramPairs){
					String[] values = paramPair.split("=");
					if(values.length!=2){
						throw new IllegalStateException("Param pair expected to be in format name=value bu found" + paramPair);
					}
					model.addParam(values[0], values[1]);
				}
			}
		}
	}





}
