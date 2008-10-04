package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldAccessor {

	protected Class<?> type;
	protected String name;
	protected Field field;

	public FieldAccessor(Class<?> type, String fieldName){
		this.type = type;
		this.name = fieldName.toLowerCase();

		load();
	}

	protected void load(){


		Field[] fields = type.getDeclaredFields();
		for (Field f : fields){
			if(f.getName().equalsIgnoreCase(name)){
				field = f;
				break;
			}
		}
		if (field==null){
			throw new ReflectionException("Field " + name + " not found");
		}
	}
	
	FieldAccessor(Class<?> type, Field field) {
		this.type = type;
		this.name = field.getName().toLowerCase();
		this.field = field;
	}

	public Object getValue(Object target) throws ReflectionException{
		try {
			field.setAccessible(true);
			return field.get(target);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public void  setValue(Object target, Object value )  throws ReflectionException{
		try {
			field.set(target,value);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public String getName() {
		return name;
	}

	public Class<?> getParentClass(){
		return type;
	}

	public Class<?> getValueType(){
		return field.getType();
	}

	public <A extends Annotation> boolean isAnnotatedWith(Class<A> annotationClass) {
		return (field!=null && field.isAnnotationPresent(annotationClass));
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return ReflectionUtils.getAnnotation(field,annotationClass);
	}
}
