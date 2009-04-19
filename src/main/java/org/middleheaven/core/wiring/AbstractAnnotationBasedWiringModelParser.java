package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.util.classification.BooleanClassifier;

public abstract class AbstractAnnotationBasedWiringModelParser implements
WiringModelParser {

	protected WiringSpecification[] readParamsSpecification(Constructor constructor, BooleanClassifier<Annotation> filter){
		return parseAnnotations(
				constructor.getParameterAnnotations(),
				constructor.getParameterTypes(),
				filter
		);
	}
	protected WiringSpecification[] readParamsSpecification(Method method, BooleanClassifier<Annotation> filter){
		return parseAnnotations(
				method.getParameterAnnotations(),
				method.getParameterTypes(),
				filter
		);
	}

	private WiringSpecification[] parseAnnotations(Annotation[][] constructorAnnnnotations ,Class<?>[] types,BooleanClassifier<Annotation> filter){

		Set[] specs = new Set[types.length];
		WiringSpecification[] params = new WiringSpecification[types.length];

		for (int p =0; p< constructorAnnnnotations.length;p++){
			// inner classes have a added parameter on index 0 that 
			// get annotations does not cover.
			// read from end to start

			int typeIndex = types.length - 1 - p;
			int annotIndex = constructorAnnnnotations.length - 1 -p;

			specs[typeIndex] = new HashSet();

			for (Annotation a : constructorAnnnnotations[annotIndex]){

				if (filter.classify(a).booleanValue()){
					specs[typeIndex].add(a);	
				}
			}

			params[typeIndex] = WiringSpecification.search(types[typeIndex],specs[typeIndex]);
		}

		return params;
	}
}
