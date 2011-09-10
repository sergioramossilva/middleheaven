package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.core.reflection.ClassCastReflectionException;
import org.middleheaven.core.reflection.IllegalAccessReflectionException;
import org.middleheaven.core.reflection.InvocationTargetReflectionException;
import org.middleheaven.core.reflection.NoSuchClassReflectionException;
import org.middleheaven.core.reflection.NoSuchMethodReflectionException;
import org.middleheaven.core.reflection.ProxyHandler;


/**
 * 
 *
 * @param <T> the introspected class
 */
public class ClassIntrospector<T> extends Introspector{

	private Class<T> type;

	public static boolean isInClasspath(String className) {
		try {
			Class.forName(className, false, ClassIntrospector.class.getClassLoader());
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	ClassIntrospector(Class<T> type) {
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	public static ClassIntrospector<?> loadFrom( String className){
		// this does not delegate to load(string,classloaded) because inicialization control
		try{
			Class type =  Class.forName(className, false, ClassIntrospector.class.getClassLoader());
			return new ClassIntrospector(type);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}
	
	/**
	 * Loads a subclass of the introspected class from its name.
	 * 
	 * @param className
	 * @return a {@code ClassIntrospector} for the loaded class
	 * @throws NoSuchClassReflectionException if the class is not found on the classpath
	 * @throws ClassCastReflectionException if the loaded class is not a subclass of the introspected class 
	 */
	public ClassIntrospector<T> load( String className){
		// this does not delegate to load(string,classloaded) because inicialization control
		try{
			@SuppressWarnings("unchecked")
			Class<T> type = (Class<T>) Class.forName(className, false, this.getClass().getClassLoader()).asSubclass(this.type);
			return new ClassIntrospector<T>(type);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		} catch (ClassCastException e){
			throw new ClassCastReflectionException(className , this.type.getName());
		}
	}

	/**
	 * Loads a subclass of the introspected class from its name using the given {@code ClassLoader}.
	 * 
	 * @param className
	 * @return a {@code ClassIntrospector} for the loaded class
	 * @throws NoSuchClassReflectionException if the class is not found on the classpath
	 * @throws ClassCastReflectionException if the loaded class is not a subclass of the introspected class 
	 */
	
	public ClassIntrospector<T> load( String className, ClassLoader loader){
		try{
			@SuppressWarnings("unchecked")
			Class<T> type = (Class<T>) loader.loadClass(className).asSubclass(this.type);
			return new ClassIntrospector<T>(type);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	
	public Class<T> getIntrospected(){
		return this.type;
	}
	
	public T cast (Object object){
		return type.cast(object);
	}

	public String getCanonicalName (){
		return type.getCanonicalName();
	}

	public String getName (){
		return type.getName();
	}

	public String getSimpleName (){
		return type.getSimpleName();
	}

	public Type[] getActualTypeArguments(){
		Type type  = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType){
			ParameterizedType parameterizedType = (ParameterizedType) type;
			return parameterizedType.getActualTypeArguments();
		} else {
			return new Type[0];
		}
		
	}
	public IntrospectionCriteriaBuilder<T> inspect(){
		return new IntrospectionCriteriaBuilder<T>(this.type);
	}

	public PackageIntrospector getPackage (){
		return Introspector.of(type.getPackage());
	}

	public T newInstance (Object ... args){
		return Reflector.getReflector().newInstance(this.type, args);
	}

	public T newInstance (ClassLoader otherClassLoader){
		return Reflector.getReflector().newInstance(this.type, otherClassLoader);
	}

	public T newProxyInstance(ProxyHandler handler){
//		if(!type.isInterface()){
//			throw new UnsupportedOperationException("Cannot proxy " + type.getName() + " as interface");
//		}
		return Reflector.getReflector().proxyType(type, handler);
	} 
	
	public <I> I newProxyInstance(ProxyHandler handler, Class<I> proxyInterface ,Class<?> ... adicionalInterfaces){
		if(!type.isInterface()){
			throw new UnsupportedOperationException("Cannot proxy " + type.getName() + ".Type is not an interface");
		}
		return Reflector.getReflector().proxyType(type, handler, proxyInterface, adicionalInterfaces);
	} 

	public boolean isInstance (Object obj){
		return type.isInstance(obj);
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return type.getAnnotation(annotationClass);
	}

	@Override
	public <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass) {
		return type.isAnnotationPresent(annotationClass);
	}

	public void invokeMain(String ... params) {
		try {
			Method methodToInvoke = type.getMethod("main", String[].class);
			methodToInvoke.setAccessible(true);
			final Object args = params; 
			methodToInvoke.invoke(null, args);
		} catch (SecurityException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodReflectionException(e);
		}
	}

	public boolean isEnhanced() {
		return Reflector.getReflector().isEnhanced(type);
	}

	public Class<?>[] getImplementedInterfaces() {
		return type.getInterfaces(); 
	}

	public Class<?>[] getDeclaredInterfaces() {
		List<Class<?>> all = new LinkedList<Class<?>>();
		Class<?> superType = type;
		while (superType!=null && !superType.equals(Object.class)){
			all.addAll(Arrays.asList(superType.getInterfaces())); // interfaces in class
			
			// up to super class
			superType = superType.getSuperclass();
		}

		Class<?>[] result = new Class<?>[all.size()]; 
		return all.toArray(result);
	}

	/**
	 * Informs if the instrospected type is fundamental. Fundamental types are thoses primitive, respective wrappers,
	 * {@code String} and {@code Date}
	 * 
	 * @return {@code true} if the instrospected type is fundamental, {@code false] otherwise.
	 */
	public boolean isFundamental() {
		return type.isPrimitive()
			|| String.class.isAssignableFrom(type)
			|| Date.class.isAssignableFrom(type)
			|| Integer.class.isAssignableFrom(type)
			|| Long.class.isAssignableFrom(type)
			|| BigInteger.class.isAssignableFrom(type)
			|| BigDecimal.class.isAssignableFrom(type)
			|| Byte.class.isAssignableFrom(type)
			|| Short.class.isAssignableFrom(type)
			|| Character.class.isAssignableFrom(type)
			|| Double.class.isAssignableFrom(type)
			|| Float.class.isAssignableFrom(type)
			;
	}


}
