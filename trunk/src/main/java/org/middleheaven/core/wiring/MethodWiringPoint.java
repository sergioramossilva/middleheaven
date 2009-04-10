package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.ReflectionUtils;

public class MethodWiringPoint implements AfterWiringPoint{

	Method method;
	
	public MethodWiringPoint(Method method) {
		super();
		this.method = method;
	}

	public <T> T writeAtPoint(EditableBinder binder, T object){
		if(object ==null){
			return null;
		}
		
		method.setAccessible(true);

		Annotation[][] annotations = method.getParameterAnnotations();
		Class<?>[] types = method.getParameterTypes();
		Set[] specs = new Set[types.length];
		Object[] params = new Object[types.length];

		for (int p =0; p< types.length;p++){
			specs[p] = new HashSet();
			for (Annotation a : annotations[p]){
				if (a.annotationType().isAnnotationPresent(BindingSpecification.class) || 
						a.annotationType().isAnnotationPresent(ScopeSpecification.class)){
					specs[p].add(a);
				}
			}
			params[p] = binder.getInstance(WiringSpecification.search(types[p], specs[p]));
		}


		try {
			method.invoke(object, params);
		} catch (Exception e) {
			ReflectionException.manage(e, object.getClass());
		} 
		
		return object;
	}
}
