package org.middleheaven.core.reflection.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.middleheaven.core.reflection.IllegalAccessReflectionException;
import org.middleheaven.core.reflection.InstantiationReflectionException;
import org.middleheaven.core.reflection.InvocationTargetReflectionException;
import org.middleheaven.core.reflection.NoSuchClassReflectionException;
import org.middleheaven.core.reflection.NoSuchMethodReflectionException;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.ReflectionStrategy;
import org.middleheaven.core.reflection.WrapperProxy;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.function.Predicate;

class Reflector {

	private  ReflectionStrategy stategy;

	private Reflector(){
		stategy = new CGLibReflectionStrategy();
	}

	private final static Reflector ME = new Reflector();
	
	protected static Reflector getReflector () {
		return ME;
	}
	
	public  boolean isEnhanced(Class<?> type) {
		return stategy.isEnhanced(type);
	}

	public  boolean isInClasspath(String className) {
		try {
			Class.forName(className, false, Reflector.class.getClassLoader());
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public  Object unproxy(Object proxy){
		if (proxy instanceof WrapperProxy){
			return ((WrapperProxy)proxy).getWrappedObject();
		}
		return null;
	}

	public  <T> T proxyType (Class<T> facadeClass , final ProxyHandler delegator){
		return stategy.proxyType(facadeClass, delegator);
	}

	public  <T> T proxyType (Class<?> facadeClass , final ProxyHandler delegator,Class<T> proxyInterface , Class<?> ... adicionalInterfaces){
		return stategy.proxyType( facadeClass,delegator,proxyInterface,adicionalInterfaces);
	}

	public  <I> I proxyObject ( Object delegationTarget , Class<I> proxyInterface){
		return stategy.proxyObject(delegationTarget, proxyInterface);
	}

	public  <I> I proxyObject (Object delegationTarget , final ProxyHandler delegator , Class<I> proxyInterface ){
		return stategy.proxyObject(delegationTarget, delegator, proxyInterface);
	}

	public  <I> I proxyObject (Object delegationTarget , final ProxyHandler delegator , Class<I> proxyInterface , Class<?> ... adicionalInterfaces){
		return stategy.proxyObject(delegationTarget, delegator, proxyInterface,adicionalInterfaces);
	}

	

	public  Class<?> getRealType(Class<?> type){
		return stategy.getRealType(type);
	}
	/*
	public  <T> T copy(T original , T copy ){

		for (PropertyAccessor fa :  getPropertyAccessors(original.getClass())){
			fa.setValue(copy, fa.getValue(original));
		}
		return copy;
	}
	 */

	public  PropertyAccessor getPropertyAccessor(Class<?> type, String fieldName){
		return stategy.getPropertyAccessor(type, fieldName);
	}

	public  Enumerable<PropertyAccessor> getPropertyAccessors(Class<?> type)throws ReflectionException{
		return stategy.getPropertyAccessors(type);
	}

	/**
	 * Determines if class <code>match</code> is a superclass or supper-interface
	 * of class <code>test</code> 
	 * @param test
	 * @param match
	 * @return
	 */
	public  boolean isAssignableFrom (Class<?> test , Class<?> match){
		return match.isAssignableFrom(test);
	}

	public  Object compareField (Object obj , String fieldName)throws ReflectionException{

		for (Method m : getMethods(obj.getClass())){
			String name = m.getName().toLowerCase();
			if ( (name.startsWith("get") || name.startsWith("is")) && name.endsWith(fieldName)){
				try {
					return m.invoke(obj, new Object[0]);
				} catch (IllegalArgumentException e) {
					throw new IllegalAccessReflectionException(e);
				} catch (IllegalAccessException e) {
					throw new IllegalAccessReflectionException(e);
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
	 * @throws NoSuchClassReflectionException if the class is not found in the classpath
	 *  
	 */
	public  Class<?> loadClass(String className) throws InstantiationReflectionException{
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public  <T> Class<? extends T> loadClass(String className, Class<T> superType) throws InstantiationReflectionException{
		try {
			return Class.forName(className).asSubclass(superType);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public  <T> Class<? extends T> loadClass(String className, Class<T> superType, ClassLoader cloader) {
		try {
			return cloader.loadClass(className).asSubclass(superType);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public  <T> T newInstance(String className , Class<T> type, ClassLoader cloader) {
		try {
			return  type.cast(newInstance(Class.forName(className, true, cloader)));
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public  <T> T newInstance(String className, Class<T> type) throws NoSuchClassReflectionException {
		try {
			return  type.cast(newInstance(Class.forName(className)));
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}

	}

	public  <T> T newInstance(Class<T> klass, Object ... args) throws ReflectionException{
		return newInstance(klass,klass,args);
	}

	public  <T> T newInstance(Class<T> castAs,Class<?> klass, Object ... args) throws ReflectionException{
		try {

			// look for a Constructor with the correct arguments
			Class<?> [] parameterTypes = new Class<?> [args.length];
			for (int i=0;i<args.length;i++){
				parameterTypes[i] = args[i].getClass();
			}

			try {

				Constructor<?> c = klass.getConstructor(parameterTypes);
				c.setAccessible(true);
				return castAs.cast(c.newInstance(args)); // Instantiate using the constructor
			} catch (SecurityException e) {
				throw new IllegalAccessReflectionException(e);
			} catch (NoSuchMethodException e) {
				throw new NoSuchMethodReflectionException(e);
			} catch (IllegalArgumentException e) {
				throw new IllegalAccessReflectionException(e);
			} catch (InvocationTargetException e) {
				throw new InvocationTargetReflectionException(e);
			}

		} catch (InstantiationException e){
			throw new ReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		}
	}

	/**
	 * Returns all fields annotated with a determined annotation
	 * @param class1
	 * @param class2
	 */
	public  Set<Field> allAnnotatedFields(Class<?> type, Class<? extends Annotation> ... annotations) {

		Field[] fields = type.getDeclaredFields();

		Set<Field> annotated = new HashSet<Field>();

		for (Field f : fields){
			for (Class<? extends Annotation> a : annotations){
				if (f.isAnnotationPresent(a)){
					annotated.add(f);
				}
			}
		}

		return annotated;
	}

	@SuppressWarnings("unchecked")
	public  <T> List<Constructor<T>> allAnnotatedConstructors( Class<T> type, Class<? extends Annotation> ... annotations) {
		Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();

		List<Constructor<T>> annotated = new ArrayList<Constructor<T>>(constructors.length);

		for (Constructor<T> c : constructors){
			for (Class<? extends Annotation> a : annotations){
				if (c.isAnnotationPresent(a)){
					annotated.add(c);
				}
			}
		}

		return annotated;
	}

	public  Set<Method> allAnnotatedMethods(Class<?> type, Class<? extends Annotation> ... annotations) {

		Set<Method> all = new HashSet<Method>();

		// read all methods in the hierarchy
		Class<?> superType = type;
		while (!superType.equals(Object.class)){
			all.addAll(Arrays.asList(superType.getDeclaredMethods())); // private , protected , public
			all.addAll(Arrays.asList(superType.getMethods())); // super class inherit public

			superType = superType.getSuperclass();

		}


		Set<Method> annotated = new HashSet<Method>();

		for (Method m : all){
			for (Class<? extends Annotation> a : annotations){
				if (m.isAnnotationPresent(a)){
					annotated.add(m);
					break;
				}
			}
		}

		return annotated;
	}

	public  Class<?>[] typesOf (Object[] objs ){
		Class<?>[] classes = new Class<?>[objs.length];
		for (int i =0;i < objs.length;i++){
			classes[i] = objs[i].getClass();
		}
		return classes;
	}

	public  Set<Annotation> getAnnotations(Class<?> type) {
		// read all methods in the hierarchy
		Set<Annotation> all = new HashSet<Annotation>();
		Class<?> superType = type;
		while (superType!=null && !superType.equals(Object.class)){
			all.addAll(Arrays.asList(superType.getAnnotations())); // pannotations in class
			for (Class<?> in : superType.getInterfaces()){
				// annotations in interfaces
				all.addAll(Arrays.asList(in.getAnnotations()));
			}
			// up to super class
			superType = superType.getSuperclass();
		}

		return all;

	}

	public  Set<Annotation> getAnnotations(Field f, Class<? extends Annotation> specificAnnotation) {

		Set<Annotation> result = new HashSet<Annotation>(); 

		Annotation[] all = f.getDeclaredAnnotations();
		for (Annotation a : all){
			if (a.annotationType().isAnnotationPresent(specificAnnotation)){
				result.add(a);
			}
		}
		return result;
	}

	/*
	public  Set<Field> getAllFields(Class<?> type) {
		Set<Field> fields = new HashSet<Field>();


		for (Field f : type.getDeclaredFields()){
			fields.add(f);
		}

		return fields;
	}


	public  void invokeMain(Class<?> mainClass,  String ... params) {
		try {
			Method methodToInvoke = mainClass.getMethod("main", String[].class);
			methodToInvoke.setAccessible(true);
			final Object[] args = params; 
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
	 */
	public  <T> T invokeStatic(Class<T> returnType,Method methodToInvoke, Object ... params) {
		try {
			methodToInvoke.setAccessible(true);
			return returnType.cast(methodToInvoke.invoke(null, params));
		} catch (SecurityException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		}
	}

	public  <T> T invoke(Class<T> returnType,Method methodToInvoke, Class<?> translatingObjectClass, Object ... params) {
		return invoke(returnType, methodToInvoke, newInstance(translatingObjectClass), params);
	}

	public  <T> T invoke(Class<T> returnType,Method methodToInvoke, Object translatingObject, Object ... params) {
		try {
			methodToInvoke.setAccessible(true);
			Object obj = methodToInvoke.invoke(translatingObject, params);
			if (returnType!=null){
				return returnType.cast(obj);
			} else {
				return null;
			}
		} catch (SecurityException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetReflectionException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		}
	}

	/**
	 * Resolves all constructors for a type and returns them in a list ordered by number of parameters.
	 * @param <T>
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	/*
	public  <T> List<Constructor<T>> constructors(Class<T> type){
		Constructor<T>[] constructors = (Constructor<T>[])type.getConstructors();

		Arrays.sort(constructors, new Comparator<Constructor<T>>(){

			@Override
			public int compare(Constructor<T> a, Constructor<T> b) {
				return a.getParameterTypes().length - b.getParameterTypes().length;
			}

		});

		return Arrays.asList(constructors);
	}
	 */


	private  Map<String, Map<MethodKey , Method >> classMethods = new WeakHashMap<String, Map<MethodKey , Method >>();
	private  Map<String, Map<FieldKey , Field >> classFields = new WeakHashMap<String, Map<FieldKey , Field >>();

	private final  class MethodKey {

		String id;
		int hash;

		public MethodKey(Class<?> type, String name, Class<?>[] parameterTypes) {
			StringBuilder builder = new StringBuilder( type.getName())
			.append('#').append(name).append('(');

			for (int i=0; i < parameterTypes.length;i++){
				if (i>0){
					builder.append(',');
				}
				builder.append(parameterTypes[i].getName());
			}

			this.id = builder.toString();
			hash = id.hashCode();
		}

		public boolean equals(Object other){
			return (other instanceof MethodKey) && ((MethodKey)other).id.equals(this.id);
		}

		public int hashCode(){
			return hash;
		}
	}
	
	private final  class FieldKey {

		String id;
		int hash;

		public FieldKey(Class<?> type, String name) {
			StringBuilder builder = new StringBuilder( type.getName())
			.append('#').append(name);

			this.id = builder.toString();
			hash = id.hashCode();
		}

		public boolean equals(Object other){
			return (other instanceof FieldKey) && ((FieldKey)other).id.equals(this.id);
		}

		public int hashCode(){
			return hash;
		}
	}

	private  Map<MethodKey , Method> getClassMethods (Class<?> type){
		Map<MethodKey , Method>  methods = classMethods.get(type.getName());

		if (methods == null){
			methods = new HashMap<MethodKey , Method>();
			classMethods.put(type.getName(), methods);
			for (Method m : type.getMethods()){
				methods.put(new MethodKey(type,m.getName(),m.getParameterTypes()), m);
			}
			for (Method m : type.getDeclaredMethods()){
				methods.put(new MethodKey(type,m.getName(),m.getParameterTypes()), m);
			}
		}

		return methods;
	}
	
	private  Map<FieldKey , Field> getClassFields (Class<?> type){
		Map<FieldKey , Field>  fields = classFields.get(type.getName());

		if (fields == null){
			fields = new HashMap<FieldKey , Field>();
			classFields.put(type.getName(), fields);
			for (Field m : type.getFields()){
				fields.put(new FieldKey(type,m.getName()), m);
			}
			for (Field m : type.getDeclaredFields()){
				fields.put(new FieldKey(type,m.getName()), m);
			}
		}

		return fields;
	}

	public  Method getMethod (Class<?> type , String name, Class<?>[] paramTypes){
		MethodKey key = new MethodKey(type,name,paramTypes);
		return getClassMethods(type).get(key);
	}

	public  List<Method> getMethods(Class<?> type) {
		return getMethods(type,null);
	}

	public  List<Method> getMethods(Class<?> type, Predicate<Method> filter) {


		List<Method> result = new ArrayList<Method>(getClassMethods(type).values());

		if (filter!=null){
			for (Iterator<Method> it = result.iterator();it.hasNext(); ){
				if (!filter.apply(it.next())){
					it.remove();
				}
			}
		}

		return result;

	}

	/**
	 * @param type
	 * @return
	 */
	public List<Field> getFields(Class<?> type) {
		return new ArrayList<Field>(getClassFields(type).values());
	}





















}
