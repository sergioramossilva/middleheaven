package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionUtils;
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

	@Wire 
	private WiringSpecification[] parseAnnotations(Annotation[][] constructorAnnnnotations ,Class<?>[] types,BooleanClassifier<Annotation> filter,boolean isRequired, boolean isShareable){

		Set[] specs = new Set[types.length];
		WiringSpecification[] params = new WiringSpecification[types.length];

		for (int p =0; p< constructorAnnnnotations.length;p++){
			// inner classes have a added parameter on index 0 that 
			// get annotations does not cover.
			// read from end to start

			int typeIndex = types.length - 1 - p;
			int annotIndex = constructorAnnnnotations.length - 1 -p;

			specs[typeIndex] = new HashSet();

			boolean isParamRequired = isRequired;
			boolean isParamShareable = isShareable;
			for (Annotation a : constructorAnnnnotations[annotIndex]){

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
