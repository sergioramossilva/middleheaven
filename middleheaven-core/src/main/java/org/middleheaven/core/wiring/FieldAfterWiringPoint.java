package org.middleheaven.core.wiring;

import java.lang.reflect.Field;

import org.middleheaven.core.reflection.ReflectionException;

/**
 * Used a {@link Field} as a {@link AfterWiringPoint}.
 */
public class FieldAfterWiringPoint implements AfterWiringPoint{

	private Field field;
	private WiringSpecification<?> specs;
	
	/**
	 * 
	 * Constructor.
	 * @param f the field that objects will be wired to..
	 * @param specs the wiring specification. 
	 */
	public FieldAfterWiringPoint(Field f, WiringSpecification<?> specs) {
		super();
		this.field = f;
		this.specs = specs;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode(){
		return field.hashCode();
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean equals(Object other){
		return other instanceof FieldAfterWiringPoint && this.field.equals(((FieldAfterWiringPoint)other).field);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
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

	@Override
	public boolean isRequired() {
		return specs.isRequired();
	}
}
