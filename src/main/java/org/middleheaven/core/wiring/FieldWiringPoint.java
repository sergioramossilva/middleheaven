package org.middleheaven.core.wiring;

import java.lang.reflect.Field;

import org.middleheaven.core.reflection.ReflectionException;

public class FieldWiringPoint implements AfterWiringPoint{

	private Field field;
	private WiringSpecification<?> specs;
	
	public FieldWiringPoint(Field f, WiringSpecification<?> specs) {
		super();
		this.field = f;
		this.specs = specs;
	}

	public int hashCode(){
		return field.hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof FieldWiringPoint && this.field.equals(((FieldWiringPoint)other).field);
	}
	
	public <T> T writeAtPoint(EditableBinder binder, T object){
		if(object ==null){
			return null;
		}
		
		field.setAccessible(true);

		Object value = binder.getInstance(specs);
		
		if(!field.getType().isAssignableFrom(value.getClass())){
			throw new BindingException(value.getClass().getName() + " can not be assigned to " + field.getType().getName());
		}
		try {
			if (value==null && specs.isRequired()){
				throw new BindingException("Value to bind with " + field.getDeclaringClass().getName() +"." + field.getName()+ "was not found ");
			}
			field.set( object, value );
		} catch (Exception e) {
			ReflectionException.manage(e, object.getClass());
		}
		
		return object;
	}
}
