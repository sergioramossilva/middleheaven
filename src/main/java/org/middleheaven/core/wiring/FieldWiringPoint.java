package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.ReflectionUtils;

public class FieldWiringPoint implements AfterWiringPoint{

	Field f;
	
	public FieldWiringPoint(Field f) {
		super();
		this.f = f;
	}

	public <T> T writeAtPoint(EditableBinder binder, T object){
		if(object ==null){
			return null;
		}
		
		f.setAccessible(true);

		Set<Annotation> specs = ReflectionUtils.getAnnotations(f, BindingSpecification.class);
		Object value = binder.getInstance(WiringSpecification.search(f.getType(), specs));
		if(!f.getType().isAssignableFrom(value.getClass())){
			throw new BindingException(value.getClass().getName() + " can not be assigned to " + f.getType().getName());
		}
		try {
			f.set( object, value );
		} catch (Exception e) {
			ReflectionException.manage(e, object.getClass());
		}
		
		return object;
	}
}
