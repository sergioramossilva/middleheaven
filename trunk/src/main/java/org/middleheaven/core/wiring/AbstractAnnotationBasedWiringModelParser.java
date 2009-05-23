package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.wiring.annotations.Name;
import org.middleheaven.core.wiring.annotations.Params;
import org.middleheaven.core.wiring.annotations.ScopeSpecification;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.util.classification.BooleanClassifier;

public abstract class AbstractAnnotationBasedWiringModelParser implements WiringModelReader {

	protected WiringSpecification[] readParamsSpecification(Constructor constructor, BooleanClassifier<Annotation> filter){
		return parseAnnotations(
				constructor.getParameterAnnotations(),
				constructor.getParameterTypes(),
				filter,
				true,
				true
				
		);
	}
	protected WiringSpecification[] readParamsSpecification(Method method, BooleanClassifier<Annotation> filter){
		boolean required =true;
		boolean shareable = true;
		if(method.isAnnotationPresent(Wire.class)){
			Wire wire = ReflectionUtils.getAnnotation(method, Wire.class);
			required = wire.required();
			shareable = wire.shareabled();
		}
		return parseAnnotations(
				method.getParameterAnnotations(),
				method.getParameterTypes(),
				filter, required , shareable
		);
	}
	
	protected WiringSpecification readParamsSpecification(Field field, BooleanClassifier<Annotation> filter){
		boolean required =true;
		boolean shareable = true;
		if(field.isAnnotationPresent(Wire.class)){
			Wire wire = ReflectionUtils.getAnnotation(field, Wire.class);
			required = wire.required();
			shareable = wire.shareabled();
		}
		return parseAnnotations(
				field.getAnnotations(),
				field.getType(),
				filter, required , shareable
		);
	}

	private WiringSpecification parseAnnotations(Annotation[] annnnotations ,Class<?> type,BooleanClassifier<Annotation> filter,boolean isRequired, boolean isShareable){

			Set specs = new HashSet();
			boolean isParamRequired = isRequired;
			boolean isParamShareable = isShareable;
			for (Annotation a : annnnotations){

				if (Wire.class.isAssignableFrom(a.annotationType())){
					Wire wire = (Wire)a;
					isParamRequired = wire.required();
					isParamShareable = wire.shareabled();
				}

				if (filter.classify(a).booleanValue()){
					specs.add(a);	
				}
			}

			WiringSpecification params = WiringSpecification.search(type,specs);

			params.setRequired(isParamRequired);
			params.setShareable(isParamShareable);
		
			return params;
	}

	private WiringSpecification[] parseAnnotations(Annotation[][] annnnotations ,Class<?>[] types,BooleanClassifier<Annotation> filter,boolean isRequired, boolean isShareable){

		Set[] specs = new Set[types.length];
		WiringSpecification[] params = new WiringSpecification[types.length];

		for (int p =0; p< annnnotations.length;p++){
			// inner classes have a added parameter on index 0 that 
			// get annotations does not cover.
			// read from end to start

			int typeIndex = types.length - 1 - p;
			int annotIndex = annnnotations.length - 1 -p;

			specs[typeIndex] = new HashSet();

			boolean isParamRequired = isRequired;
			boolean isParamShareable = isShareable;
			for (Annotation a : annnnotations[annotIndex]){

				if (Wire.class.isAssignableFrom(a.annotationType())){
					Wire wire = (Wire)a;
					isParamRequired = wire.required();
					isParamShareable = wire.shareabled();
				}
				if (filter.classify(a).booleanValue()){
					specs[typeIndex].add(a);	
				}
			}

			params[typeIndex] = WiringSpecification.search(types[typeIndex],specs[typeIndex]);

			params[typeIndex].setRequired(isParamRequired);
			params[typeIndex].setShareable(isParamShareable);
		}

		return params;
	}
}
