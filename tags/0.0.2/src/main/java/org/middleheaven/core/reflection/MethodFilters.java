package org.middleheaven.core.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.middleheaven.util.classification.BooleanClassifier;


/**
 * Utility class for use with ReflectionUtils to filter methods in a class
 *
 */
public class MethodFilters implements BooleanClassifier<Method> {

	/**
	 * 
	 * @return methods that are public, non-static and that do not 
	 * conform to the get/set convention
	 */
	public static MethodFilters publicInstanceNonProperty(){
		return new MethodFilters(){

			@Override
			public Boolean classify(Method method) {
				final String name = method.getName();
				final int modifiers = method.getModifiers();
				return Boolean.valueOf(Modifier.isPublic(modifiers) && !(name.startsWith("get") || name.startsWith("set") || name.startsWith("is")));
			}
		};
	}

	public static MethodFilters publicProperties(){
		return new MethodFilters(){

			@Override
			public Boolean classify(Method method) {
				final String name = method.getName();
				final int modifiers = method.getModifiers();
				return Boolean.valueOf(Modifier.isPublic(modifiers) && !name.equals("getClass") && (name.startsWith("get") || name.startsWith("set") || name.startsWith("is")));
			}
		};

	}

	public static MethodFilters all(){
		return new MethodFilters();
	}

	private MethodFilters(){

	}


	@Override
	public Boolean classify(Method method) {
		return Boolean.TRUE;
	}

}
