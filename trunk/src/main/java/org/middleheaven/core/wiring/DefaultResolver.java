package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.middleheaven.core.reflection.IllegalAccesReflectionException;
import org.middleheaven.core.reflection.InstantiationReflectionException;
import org.middleheaven.core.reflection.InvocationTargetReflectionException;
import org.middleheaven.core.reflection.ReflectionUtils;

public class DefaultResolver<T, Base extends T> implements Resolver<T> {

	EditableBinder binder;
	Class<Base> type;

	public DefaultResolver(Class<Base> type , EditableBinder binder){
		this.binder = binder;
		this.type = type;
	}
	
	@Override
	public T resolve(Class<T> targetType, Set<Annotation> annotations) {
		// Determine witch constructor to invoke

		List<Constructor<Base>> constructors =  ReflectionUtils.allAnnotatedConstructors( type, Wire.class);

		Constructor<?> selectedConstructor;
		if (constructors.isEmpty()){
			// search not annotated constructors
			constructors = ReflectionUtils.constructors(type);
			if (constructors.size()>1){
				throw new ConfigurationException("Multiple constructors found. Annotate one with @" + Wire.class.getSimpleName());
			}
		} else if (constructors.size()>1){
			throw new ConfigurationException("Only one constructor may be annotated with @" + Wire.class.getSimpleName());
		} 
			
		// by this line, there is only one constructor found
		selectedConstructor  = constructors.get(0);
		selectedConstructor.setAccessible(true);

		Annotation[][] constructorAnnnnotations = selectedConstructor.getParameterAnnotations();
		Class<?>[] types = selectedConstructor.getParameterTypes();
		Set[] specs = new Set[types.length];
		Object[] objects = new Object[types.length];


		for (int p =0; p< types.length;p++){
			specs[p] = new HashSet();
			for (Annotation a : constructorAnnnnotations[p]){
				if (a.annotationType().isAnnotationPresent(BindingSpecification.class) ||
						a.annotationType().isAnnotationPresent(ScopeSpecification.class)){
					specs[p].add(a);
				}
			}
			objects[p] = binder.getInstance(types[p], specs[p]);
		}


		try {
			return targetType.cast(selectedConstructor.newInstance(objects));
		} catch (Exception e) {
			throw handleExceptions(targetType,e);
		} 

	}

	private static RuntimeException handleExceptions(Class<?> type, Throwable t){
		try {
			throw t;
		} catch (SecurityException e) {
			throw new IllegalAccesReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccesReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (InstantiationException e) {
			throw new InstantiationReflectionException(type.getName(), e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IllegalAccesReflectionException(e);
		} catch (RuntimeException e) {
			throw e;
		} catch (Error e) {
			throw e;
		} catch (Throwable e){
			return new RuntimeException(e);
		}
	}
}
