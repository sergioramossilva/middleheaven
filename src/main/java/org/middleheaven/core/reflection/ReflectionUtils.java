package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ReflectionUtils {

	public ReflectionUtils(){}


	
	public static <T> T copy(T original , T copy ){
		Collection<PropertyAccessor> assessors = getPropertyAccessors(original.getClass());

		for (PropertyAccessor fa : assessors){
			fa.setValue(copy, fa.getValue(original));
		}
		return copy;
	}
	
	public static PropertyAccessor getPropertyAccessor(Class<?> type, String fieldName){
		return new PropertyAccessor(type,fieldName);
	}
	
	public static Collection<PropertyAccessor> getPropertyAccessors(Class<?> type)
	throws ReflectionException{

		Collection<PropertyAccessor> result = new ArrayList<PropertyAccessor> ();
		for (Method m : type.getMethods()){
			if (m.getName().startsWith("set")){
				//result.add(new PropertyAccessor(type, m.getName().substring(3)));
				result.add(getPropertyAccessor(type, m.getName().substring(3)));
			}

		}
		return result;
	}
	
	/*
	public static Object getFieldValue(Object target, String name) {
		name = name.toLowerCase();
		
		Set<Field> fields = getAllFields(target.getClass());
		Field f=null;
		for (Field fd : fields){
			if (fd.getName().toLowerCase().endsWith(name)){
				f = fd;
				break;
			}
		}
		
		if (f==null){
			try {
				Method[] ms = target.getClass().getMethods();
				for (Method m : ms){
					if (m.getName().toLowerCase().endsWith(name) && m.getName().toLowerCase().contains("get")){
						m.setAccessible(true);
						return m.invoke(target);
					}
				}
				//throw  new NoSuchMethodReflectionException(name + " not found");
				return null;
			} catch (IllegalArgumentException e) {
				throw new IllegalAccesReflectionException(e);
			} catch (IllegalAccessException e) {
				throw new IllegalAccesReflectionException(e);
			}catch (InvocationTargetException e) {
				throw new InvocationTargetReflectionException(e);
			}
			
		} else {
			return getFieldValue (target,f);
		}
		
	}
	
	
	public static Object getFieldValue( Object target,Field f) {
		try {
			try {
				f.setAccessible(true);
				String prefix="get";
				if (f.getType().equals(Boolean.class)){
					prefix="is";
				}
				Method m = target.getClass().getMethod(prefix + f.getName());
				return m.invoke(target);
			} catch (NoSuchMethodException e ){
				return f.get(target); // read directly from attribute
			} 

		} catch (IllegalArgumentException e) {
			throw new IllegalAccesReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccesReflectionException(e);
		}catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		}
	}
	
	
	public static void setFieldValue (Object target,String name,  Object value){
		name = name.toLowerCase();
		
		Set<Field> fields = getAllFields(target.getClass());
		Field f=null;
		for (Field fd : fields){
			if (fd.getName().toLowerCase().endsWith(name)){
				f = fd;
				break;
			}
		}
		
		if (f==null){
			try {
				Method[] ms = target.getClass().getMethods();
				for (Method m : ms){
					if (m.getName().toLowerCase().endsWith(name) && m.getName().toLowerCase().contains("set")){
						m.setAccessible(true);
						m.invoke(target,value);
						return;
					}
				}
				//throw  new NoSuchMethodReflectionException(name + " not found");
			} catch (IllegalArgumentException e) {
				throw new IllegalAccesReflectionException(e);
			} catch (IllegalAccessException e) {
				throw new IllegalAccesReflectionException(e);
			}catch (InvocationTargetException e) {
				throw new InvocationTargetReflectionException(e);
			}
			
		} else {
			setFieldValue(f,target,value);
		}
	}
	*/
	
	/**
	 * Set a field with a value. If there is a set<Field>() method , that method is invoked
	 * otherwise the set is made directly.
	 * @param f
	 * @param value
	 */
	/*
	public static void setFieldValue (Field f, Object target, Object value){
		try {
			try {
				f.setAccessible(true);
				Method m = target.getClass().getMethod("set" + f.getName(), value.getClass());
				m.invoke(target, value);
			} catch (NoSuchMethodException e ){
				f.set(target, value);
			} 

		} catch (IllegalArgumentException e) {
			throw new IllegalAccesReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccesReflectionException(e);
		}catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		}
	}
	*/
	
	/**
	 * Determines if class <code>match</code> is a superclass or supper-interface
	 * of class <code>test</code> 
	 * @param test
	 * @param match
	 * @return
	 */
	public static boolean isAssignableFrom (Class<?> test , Class<?> match){
		return match.isAssignableFrom(test);
	}

	public static Object compareField (Object obj , String fieldName)throws ReflectionException{
		Method[] methods = obj.getClass().getMethods();

		for (Method m : methods){
			String name = m.getName().toLowerCase();
			if ( (name.startsWith("get") || name.startsWith("is")) && name.endsWith(fieldName)){
				try {
					return m.invoke(obj, new Object[0]);
				} catch (IllegalArgumentException e) {
					new IllegalAccesReflectionException(e);
				} catch (IllegalAccessException e) {
					throw new IllegalAccesReflectionException(e);
				} catch (InvocationTargetException e) {
					throw new InvocationTargetReflectionException(e);
				}
			}
		}

		throw new NoSuchMethodReflectionException(fieldName);
	}

	/**
	 * Loads a class from its name
	 * @param className
	 * @return
	 * @throws InstantiationReflectionException
	 */
	public static Class<?> loadClass(String className) throws InstantiationReflectionException{
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public static Object newInstance(String className) throws InstantiationReflectionException{
		if (className == null) {
			throw new IllegalArgumentException (className + " is not a valid value for argument className");
		}
		return newInstance(loadClass(className));
	}


	public static <T> T newInstance(Class<T> klass, Object ... args) throws ReflectionException{
		return newInstance(klass,klass,args);
	}
	public static <T> T newInstance(Class<T> castAs,Class<?> klass, Object ... args) throws ReflectionException{
		try {
			if (args.length==0){
				return castAs.cast(klass.newInstance());
			} else {
				// look for a Constructor with the correct arguments
				Class<?> [] parameterTypes = new Class<?> [args.length];
				for (int i=0;i<args.length;i++){
					parameterTypes[i] = args[i].getClass();
				}

				try {

					Constructor<?> c = klass.getConstructor(parameterTypes);
					return castAs.cast(c.newInstance(args)); // Instantiate using the constructor
				} catch (SecurityException e) {
					throw new IllegalAccesReflectionException(e);
				} catch (NoSuchMethodException e) {
					throw new NoSuchMethodReflectionException(e);
				} catch (IllegalArgumentException e) {
					throw new IllegalAccesReflectionException(e);
				} catch (InvocationTargetException e) {
					throw new InvocationTargetReflectionException(e);
				}
			}
		} catch (InstantiationException e){
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccesReflectionException(e);
		}
	}

	/**
	 * Returns all fields annotated with a determined annotation
	 * @param class1
	 * @param class2
	 */
	public static Set<Field> allAnnotatedFields(Class<?> type, Class<? extends Annotation> annotation) {

		Field[] fields = type.getDeclaredFields();

		Set<Field> annotated = new HashSet<Field>();

		for (Field f : fields){
			if (f.isAnnotationPresent(annotation)){
				annotated.add(f);
			}
		}

		return annotated;
	}

	public static <T> Set<Constructor<T>> allAnnotatedConstructors( Class<T> type, Class<? extends Annotation> annotation) {
		Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();

		Set<Constructor<T>> annotated = new HashSet<Constructor<T>>();

		for (Constructor<T> c : constructors){
			if (c.isAnnotationPresent(annotation)){
				annotated.add(c);
			}
		}

		return annotated;
	}
	
	public static Set<Method> allAnnotatedMethods(Class<?> type, Class<? extends Annotation> annotation) {
		Method[] methods = type.getDeclaredMethods();

		Set<Method> annotated = new HashSet<Method>();

		for (Method m : methods){
			if (m.isAnnotationPresent(annotation)){
				annotated.add(m);
			}
		}

		return annotated;
	}

	

	
	public static boolean isAnnotadedWith(Class<?> candidate,Class<? extends Annotation> annotationClass) {
		return candidate.isAnnotationPresent(annotationClass);
	}

	public static <A extends Annotation> A getAnnotation(Class<?> annotated, Class<A> annotationClass){
		return annotated.getAnnotation(annotationClass);
	}

	public static <A extends Annotation> A getAnnotation(AccessibleObject obj, Class<A> annotationClass){
		return obj.getAnnotation(annotationClass);
	}
	
	public static <A extends Annotation> A getAnnotation(Field field, Class<A> annotationClass) {
		if (field.isAnnotationPresent(annotationClass)){
			return field.getAnnotation(annotationClass);
		}
		return null;
	}

	public static Class<?>[] typesOf (Object[] objs ){
		Class<?>[] classes = new Class<?>[objs.length];
		for (int i =0;i < objs.length;i++){
			classes[i] = objs[i].getClass();
		}
		return classes;
	}
	
	public static Set<Annotation> getAnnotations(Field f, Class<? extends Annotation> specificAnnotation) {
		
		Set<Annotation> result = new HashSet<Annotation>(); 
		
		Annotation[] all = f.getDeclaredAnnotations();
		for (Annotation a : all){
			if (a.annotationType().isAnnotationPresent(specificAnnotation)){
				result.add(a);
			}
		}
		return result;
	}

	public static Set<Field> getAllFields(Class<?> type) {
		 Set<Field> fields = new HashSet<Field>();
		 

		 for (Field f : type.getDeclaredFields()){
			 fields.add(f);
		 }

		 return fields;
	}
	
	public static <T> T invoke(Class<T> returnType,Method methodToInvoke, Class<?> translatingObjectClass, Object ... params) {
		return invoke(returnType, methodToInvoke, newInstance(translatingObjectClass), params);
	}
	
	public static <T> T invoke(Class<T> returnType,Method methodToInvoke, Object translatingObject, Object ... params) {
		try {
			methodToInvoke.setAccessible(true);
			return returnType.cast(methodToInvoke.invoke(translatingObject, params));
		} catch (SecurityException e) {
			throw new IllegalAccesReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccesReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccesReflectionException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> List<Constructor<T>> constructors(Class<T> type){
		Constructor<T>[] constructors = (Constructor<T>[])type.getConstructors();
		
		Arrays.sort(constructors, new Comparator<Constructor<T>>(){

			@Override
			public int compare(Constructor<T> a, Constructor<T> b) {
				return a.getParameterTypes().length - b.getParameterTypes().length;
			}
			
		});
		
		return Arrays.asList(constructors);
	}
	













}
