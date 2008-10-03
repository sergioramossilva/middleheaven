package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.middleheaven.util.conversion.TypeConvertions;

public class PropertyAccessor {

	private static String capitalizeFirst(String fieldName){
		return fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
	}
	private static String unCapitalizeFirst(String fieldName){
		return fieldName.substring(0,1).toLowerCase() + fieldName.substring(1);
	}
	Class<?> type;
	String name;

	Method acessor;
	Method modifier;
	Field field;
	boolean modifyByField = false;
	
	public PropertyAccessor(Class<?> type, String fieldName) {
		super();
		this.type = type;
		this.name = capitalizeFirst(fieldName);

	

		try {
			// find corresponding field
			
			try {
				field = type.getDeclaredField(unCapitalizeFirst(name));
			} catch (NoSuchFieldException e1) {
				// property does not have a field witth the same name
				// no-op
			} 
			
			// find assessor method
			try {
				acessor = type.getMethod("get" + name, new Class<?>[0]);
			}catch (NoSuchMethodException e) {
				// try is for boolean
				try{
					acessor = type.getMethod("is" + name, new Class<?>[0]);
				}catch (NoSuchMethodException e1) {
					// try direct access to field
					if (field==null){
						 // no field and no get. the property does not exist 
						throw new PropertyNotFoundException(name + " is not a property of " + type.getName());
					}
				}
			}

			// find modifier method

			// try iterate and match 
			modifier = null;
			for (Method method : type.getMethods()){
				if (method.getName().equals("set" + name)){
					modifier = method;
					break;
				}
			}

			if (modifier==null && field!=null){
				// try access by field
				modifyByField = true;
			}

		} catch (SecurityException e) {
			throw new ReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} 
	}

	public String toString(){
		return this.name;
	}
	
	public Class<?> getParentClass(){
		return type;
	}
	
	public Class<?> getValueType(){
		return acessor!=null ? acessor.getReturnType() : field.getType();
	}
	
	public Object getValue(Object target) throws ReflectionException{
		try {
			if (acessor!=null){
				return acessor.invoke(target, new Object[0]);
			} else if (field !=null){
				field.setAccessible(true);
				return field.get(target);
			} else {
				throw new ReflectionException("Property does not exist");
			}
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(e);
		}
	}

	public void  setValue(Object target, Object value ){
		try {
			
			if (modifier != null){
				modifier.invoke(target, TypeConvertions.convert(value,modifier.getParameterTypes()[0]));
			} else if (modifyByField && field!=null){
				field.setAccessible(true);
				field.set(target,TypeConvertions.convert(value,(Class<?>)field.getGenericType()));
			} // else is read only. not an exception
		}catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(e);
		}
	}

	public String getName() {
		return name;
	}
	
	public <A extends Annotation> boolean isAnnotatedWith(Class<A> annotationClass) {
		return (field!=null && field.isAnnotationPresent(annotationClass)) || 
					acessor.isAnnotationPresent(annotationClass) || 
					(modifier!=null && modifier.isAnnotationPresent(annotationClass));
	}
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		if (field!=null){
			return ReflectionUtils.getAnnotation(field,annotationClass);
		} else {
			return ReflectionUtils.getAnnotation(acessor,annotationClass);
		}
		
	}
}

