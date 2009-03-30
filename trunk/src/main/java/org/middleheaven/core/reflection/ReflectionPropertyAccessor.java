package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.middleheaven.util.conversion.TypeConvertions;

public final class ReflectionPropertyAccessor extends ReflectionFieldAccessor implements PropertyAccessor {


	private Method acessor;
	private Method modifier;
	private boolean modifyByField = false;

	public ReflectionPropertyAccessor(Class<?> type, String fieldName) {
		super(type,fieldName);
		load();
	}

	protected void load(){

		try {
			// find corresponding field


			Field[] fields = type.getDeclaredFields();
			for (Field f : fields){
				if(f.getName().equalsIgnoreCase(name)){
					field = f;
					break;
				}
			}


			// find assessor and modifier method
			
			
			for (Method method : type.getMethods()){
				if (method.getParameterTypes().length==1 && method.getName().equalsIgnoreCase("set" + name)){
					modifier = method;
				} else if (method.getParameterTypes().length==0 && (method.getName().equalsIgnoreCase("get" + name) || 
						method.getName().equalsIgnoreCase("is" + name)) ){
					acessor = method;
				}
				if (modifier !=null && acessor != null){
					break;
				}
			}
			
			
			if (acessor ==null && field==null){
				// no field and no get. the property does not exist 
				throw new PropertyNotFoundException(name + " is not a property of " + type.getName());
			}
			
			if (modifier==null && field!=null){
				// use modification  by field
				modifyByField = true;
			}

		} catch (SecurityException e) {
			throw new ReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} 
	}

	public Class<?> getValueType(){
		return acessor!=null ? acessor.getReturnType() : field.getType();
	}

	public Object getValue(Object target) throws ReflectionException{
		try {
			if (acessor!=null){
				acessor.setAccessible(true);
				return acessor.invoke(target, new Object[0]);
			} else if (field !=null){
				field.setAccessible(true);
				return field.get(target);
			} else {
				throw new PropertyNotFoundException("Property does not exist");
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
				@SuppressWarnings("unchecked") Object obj = TypeConvertions.convert(value,(Class)field.getGenericType());
				field.set(target,obj);
			} // else is read only. not an exception
		}catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(e);
		}
	}



	public <A extends Annotation> boolean isAnnotatedWith(Class<A> annotationClass) {
		return (field!=null && field.isAnnotationPresent(annotationClass)) || 
		(acessor!=null && acessor.isAnnotationPresent(annotationClass)) || 
		(modifier!=null && modifier.isAnnotationPresent(annotationClass));
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		A a=null;
		if (field!=null){
			a =  ReflectionUtils.getAnnotation(field,annotationClass);
		} 
		
		if (a==null){
			a = ReflectionUtils.getAnnotation(acessor,annotationClass);
		}
		return a;

	}
}

