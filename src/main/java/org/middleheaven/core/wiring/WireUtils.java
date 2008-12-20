package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.reflection.IllegalAccesReflectionException;
import org.middleheaven.core.reflection.InstantiationReflectionException;
import org.middleheaven.core.reflection.InvocationTargetReflectionException;
import org.middleheaven.core.reflection.ReflectionUtils;

final class WireUtils {

	public static <T> T wireMembers (EditableBinder binder,T obj){
		Set<Field> fields = ReflectionUtils.allAnnotatedFields(obj.getClass(), Wire.class);
		try {
			for (Field f : fields){
				f.setAccessible(true);

				Set<Annotation> specs = ReflectionUtils.getAnnotations(f, BindingSpecification.class);
				Object value = binder.getInstance(WiringSpecification.search(f.getType(), specs));
				if(!f.getType().isAssignableFrom(value.getClass())){
					throw new BindingException(value.getClass().getName() + " can not be assigned to " + f.getType().getName());
				}
				f.set( obj, value );
			}

			Set<Method> methods = ReflectionUtils.allAnnotatedMethods(obj.getClass(), Wire.class);

			for (Method m : methods){
				m.setAccessible(true);

				Annotation[][] annotations = m.getParameterAnnotations();
				Class<?>[] types = m.getParameterTypes();
				Set[] specs = new Set[types.length];
				Object[] objects = new Object[types.length];

				for (int p =0; p< types.length;p++){
					specs[p] = new HashSet();
					for (Annotation a : annotations[p]){
						if (a.annotationType().isAnnotationPresent(BindingSpecification.class) || 
								a.annotationType().isAnnotationPresent(ScopeSpecification.class)){
							specs[p].add(a);
						}
					}
					objects[p] = binder.getInstance(WiringSpecification.search(types[p], specs[p]));
				}


				m.invoke(obj, objects);

			}
			
			return obj;
		} catch (Exception e) {
			throw handleExceptions(obj.getClass(),e);
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
			throw new InvocationTargetReflectionException(e.getTargetException());
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
