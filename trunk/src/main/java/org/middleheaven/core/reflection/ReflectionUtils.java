package org.middleheaven.core.reflection;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.middleheaven.util.classification.BooleanClassifier;

public final class ReflectionUtils {

	private static ReflectionStrategy stategy = new CGLibReflectionStrategy();
	
	public ReflectionUtils(){}
	
	public static <T> T proxy (Class<T> facadeClass , final ProxyHandler delegator){
		return stategy.proxy(facadeClass, delegator);
	}

	public static <I> I proxy ( Object delegationTarget , Class<I> proxyInterface){
		return stategy.proxy(delegationTarget, proxyInterface);
	}

	public static <I> I proxy (Object delegationTarget , Class<I> proxyInterface , final ProxyHandler delegator ){
		return stategy.proxy(delegationTarget, proxyInterface, delegator);
	}

	public static Set<Class> getPackageClasses(Package classPackage) {

		Set<Class> classes = new HashSet<Class>();
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

	private static void process(Set<Class> classes , URL url , String base) throws IOException{
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

	public static <T> T copy(T original , T copy ){

		for (PropertyAccessor fa :  getPropertyAccessors(original.getClass())){
			fa.setValue(copy, fa.getValue(original));
		}
		return copy;
	}

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
	 * @throws InstantiationReflectionException
	 */
	public static Class<?> loadClass(String className) throws InstantiationReflectionException{
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}
	
	
	public static <T> Class<T> loadClass(String className, Class<T> superType) throws InstantiationReflectionException{
		try {
			return (Class<T>)Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new NoSuchClassReflectionException(className);
		}
	}

	public static <T> T newInstance(String className, Class<T> type) throws ClassNotFoundReflectionException {

		try {
			return  type.cast(newInstance(Class.forName(className)));
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundReflectionException("Class " + className  + " has not found");
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

	public static <T> List<Constructor<T>> allAnnotatedConstructors( Class<T> type, Class<? extends Annotation> annotation) {
		Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();

		List<Constructor<T>> annotated = new ArrayList<Constructor<T>>(constructors.length);

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

	public static Annotation[] getAnnotations(Class<?> type) {

		return type.getDeclaredAnnotations();
	
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
	
	public static Map<MethodKey , Method> getClassMethods (Class<?> type){
		Map<MethodKey , Method>  methods = classMethods.get(type.getName());
		
		if (methods == null){
			methods = new HashMap<MethodKey , Method>();
			classMethods.put(type.getName(), methods);
			for (Method m : type.getMethods()){
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














}
