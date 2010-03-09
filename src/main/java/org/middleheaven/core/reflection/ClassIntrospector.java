package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
			return new ClassIntrospector<T>( (Class<T>) Class.forName(className, false, this.getClass().getClassLoader()).asSubclass(this.type));
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
			return new ClassIntrospector<T>((Class<T>) loader.loadClass(className).asSubclass(this.type));
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

	public IntrospectionCriteriaBuilder<T> inspect(){
		return new IntrospectionCriteriaBuilder<T>(this.type);
	}

	public PackageIntrospector getPackage (){
		return Introspector.of(type.getPackage());
	}

	public T newInstance (Object ... args){
		return ReflectionUtils.newInstance(this.type, args);
	}

	public T newInstance (ClassLoader otherClassLoader){
		return ReflectionUtils.newInstance(this.type, otherClassLoader);
	}

	public T newProxyInstance(ProxyHandler handler){
		if(!type.isInterface()){
			throw new UnsupportedOperationException("Cannot proxy " + type.getName() + " as interface");
		}
		return ReflectionUtils.proxyType(type, handler);
	} 
	
	public <I> I newProxyInstance(ProxyHandler handler, Class<I> proxyInterface ,Class<?> ... adicionalInterfaces){
		if(!type.isInterface()){
			throw new UnsupportedOperationException("Cannot proxy " + type.getName() + ".Type is not an interface");
		}
		return ReflectionUtils.proxyType(type, handler, proxyInterface, adicionalInterfaces);
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




}
