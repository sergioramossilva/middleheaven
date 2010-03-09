package org.middleheaven.core.reflection;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.middleheaven.util.classification.BooleanClassifier;

class ReflectionUtils {

	private static ReflectionStrategy stategy = new CGLibReflectionStrategy();

	public ReflectionUtils(){}

	public static boolean isInClasspath(String className) {
		try {
			Class.forName(className, false, ReflectionUtils.class.getClassLoader());
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public static Object unproxy(Object proxy){
		if (proxy instanceof WrapperProxy){
			return ((WrapperProxy)proxy).getWrappedObject();
		}
		return null;
	}

	public static <T> T proxyType (Class<T> facadeClass , final ProxyHandler delegator){
		return stategy.proxyType(facadeClass, delegator);
	}
	
	public static <T> T proxyType (Class<?> facadeClass , final ProxyHandler delegator,Class<T> proxyInterface , Class<?> ... adicionalInterfaces){
		return stategy.proxyType( facadeClass,delegator,proxyInterface,adicionalInterfaces);
	}

	public static <I> I proxyObject ( Object delegationTarget , Class<I> proxyInterface){
		return stategy.proxyObject(delegationTarget, proxyInterface);
	}

	public static <I> I proxyObject (Object delegationTarget , final ProxyHandler delegator , Class<I> proxyInterface ){
		return stategy.proxyObject(delegationTarget, delegator, proxyInterface);
	}

	public static <I> I proxyObject (Object delegationTarget , final ProxyHandler delegator , Class<I> proxyInterface , Class<?> ... adicionalInterfaces){
		return stategy.proxyObject(delegationTarget, delegator, proxyInterface,adicionalInterfaces);
	}

	public static Set<Class<?>> getPackageClasses(Package classPackage) {

		Set<Class<?>> classes = new HashSet<Class<?>>();
		try {
			ClassLoader cl = classPackage.getClass().getClassLoader();
			if (cl == null) {
				// no class loader specified -> use thread context class loader
				cl = Thread.currentThread().getContextClassLoader();
			}
			String packageUrl = classPackage.getName().replaceAll("\\.", "/");
			Enumeration<URL> packageLocations = cl.getResources(packageUrl);

			while (packageLocations.hasMoreElements()){
				URL url = packageLocations.nextElement();
				process(classes,url,classPackage.getName());
			}

			return classes;
		} catch (IOException e) {
			throw new ReflectionException(e);
		}
	}

	private static void process(Set<Class<?>> classes , URL url , String base) throws IOException{
		if (url.getProtocol().equals("file")){
			try {
				File folder = new File(url.toURI());

				File[] files = folder.listFiles(new FilenameFilter(){

					@Override
					public boolean accept(File file, String name) {
						return name.indexOf("$")<0 && name.endsWith(".class");
					}

				});

				for (File f : files){
					String name = base + "." + f.getName().substring(0, f.getName().indexOf('.'));  
					classes.add(loadClass(name));
				}
			} catch (URISyntaxException e) {
				throw new IOException(e);
			} 
		}
	}

	public static Class<?> getRealType(Class<?> type){
		return stategy.getRealType(type);
	}
	/*
	public static <T> T copy(T original , T copy ){

		for (PropertyAccessor fa :  getPropertyAccessors(original.getClass())){
			fa.setValue(copy, fa.getValue(original));
		}
		return copy;
	}
	*/

	public static PropertyAccessor getPropertyAccessor(Class<?> type, String fieldName){
		return stategy.getPropertyAccessor(type, fieldName);
	}

	public static Iterable<PropertyAccessor> getPropertyAccessors(Class<?> type)throws ReflectionException{
		return stategy.getPropertyAccessors(type);
	}

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

		for (Method m : getMethods(obj.getClass())){
			String name = m.getName().toLowerCase();
			if ( (name.startsWith("get") || name.startsWith("is")) && name.endsWith(fieldName)){
				try {
					return m.invoke(obj, new Object[0]);
				} catch (IllegalArgumentException e) {
					new IllegalAccessReflectionException(e);
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
	public static Class<?> loadClass(String className) throws InstantiationReflectionException{
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public static <T> Class<? extends T> loadClass(String className, Class<T> superType) throws InstantiationReflectionException{
		try {
			return Class.forName(className).asSubclass(superType);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}
	
	public static <T> Class<? extends T> loadClass(String className, Class<T> superType, ClassLoader cloader) {
		try {
			return cloader.loadClass(className).asSubclass(superType);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public static <T> T newInstance(String className , Class<T> type, ClassLoader cloader) {
		try {
			return  type.cast(newInstance(Class.forName(className, true, cloader)));
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}
	
	public static <T> T newInstance(String className, Class<T> type) throws NoSuchClassReflectionException {
		try {
			return  type.cast(newInstance(Class.forName(className)));
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	
	}

	public static <T> T newInstance(Class<T> klass, Object ... args) throws ReflectionException{
		return newInstance(klass,klass,args);
	}

	public static <T> T newInstance(Class<T> castAs,Class<?> klass, Object ... args) throws ReflectionException{
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
	public static Set<Field> allAnnotatedFields(Class<?> type, Class<? extends Annotation> ... annotations) {

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
	public static <T> List<Constructor<T>> allAnnotatedConstructors( Class<T> type, Class<? extends Annotation> ... annotations) {
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

	public static Set<Method> allAnnotatedMethods(Class<?> type, Class<? extends Annotation> ... annotations) {

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
	
	/*
	public static boolean isAnnotadedWith(Class<?> candidate,Class<? extends Annotation> annotationClass) {
		return candidate.isAnnotationPresent(annotationClass);
	}

	
	public static <A extends Annotation> A getAnnotation(Class<?> annotated, Class<A> annotationClass){
		return annotated.getAnnotation(annotationClass);
	}

	public static <A extends Annotation> A getAnnotation(AccessibleObject obj, Class<A> annotationClass){
		return obj.getAnnotation(annotationClass);
	}
	*/

	/*
	public static <A extends Annotation> A getAnnotation(Field field, Class<A> annotationClass) {
		if (field.isAnnotationPresent(annotationClass)){
			return field.getAnnotation(annotationClass);
		}
		return null;
	}
	*/

	public static Class<?>[] typesOf (Object[] objs ){
		Class<?>[] classes = new Class<?>[objs.length];
		for (int i =0;i < objs.length;i++){
			classes[i] = objs[i].getClass();
		}
		return classes;
	}

	public static Set<Annotation> getAnnotations(Class<?> type) {
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

	/*
	public static Set<Field> getAllFields(Class<?> type) {
		Set<Field> fields = new HashSet<Field>();


		for (Field f : type.getDeclaredFields()){
			fields.add(f);
		}

		return fields;
	}


	public static void invokeMain(Class<?> mainClass,  String ... params) {
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
	public static <T> T invokeStatic(Class<T> returnType,Method methodToInvoke, Object ... params) {
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

	public static <T> T invoke(Class<T> returnType,Method methodToInvoke, Class<?> translatingObjectClass, Object ... params) {
		return invoke(returnType, methodToInvoke, newInstance(translatingObjectClass), params);
	}

	public static <T> T invoke(Class<T> returnType,Method methodToInvoke, Object translatingObject, Object ... params) {
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
	*/


	private static Map<String, Map<MethodKey , Method >> classMethods = new WeakHashMap<String, Map<MethodKey , Method >>();

	private final static class MethodKey {

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
			return ((MethodKey)other).id.equals(this.id);
		}

		public int hashCode(){
			return hash;
		}
	}

	private static Map<MethodKey , Method> getClassMethods (Class<?> type){
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

	public static Method getMethod (Class<?> type , String name, Class<?>[] paramTypes){
		MethodKey key = new MethodKey(type,name,paramTypes);
		return getClassMethods(type).get(key);
	}

	public static List<Method> getMethods(Class<?> type) {
		return getMethods(type,null);
	}

	public static List<Method> getMethods(Class<?> type, BooleanClassifier<Method> filter) {


		List<Method> result = new ArrayList<Method>(getClassMethods(type).values());

		if (filter!=null){
			for (Iterator<Method> it = result.iterator();it.hasNext(); ){
				if (!filter.classify(it.next())){
					it.remove();
				}
			}
		}

		return result;

	}

	/*
	@SuppressWarnings("unchecked")
	public static <T> Class<T> genericClass(T object) {
		return (Class<T>) object.getClass();
	}
	*/



















}
