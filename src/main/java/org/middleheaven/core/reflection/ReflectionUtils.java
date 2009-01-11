package org.middleheaven.core.reflection;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.middleheaven.util.classification.BooleanClassifier;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

public final class ReflectionUtils {

	public ReflectionUtils(){}

	private static class MethodInvocationHandler implements InvocationHandler{

		private ProxyHandler methodHandler;
		private MethodInvocationHandler(ProxyHandler methodHandler){
			this.methodHandler =  methodHandler;
		}


		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return methodHandler.invoke(proxy,method,null,args);
		}

	}
	
	public static <T> T proxy (Class<T> facadeClass , final ProxyHandler delegator){
		try {
			if (facadeClass.isInterface()){
				return (T)Proxy.newProxyInstance(ReflectionUtils.class.getClassLoader(),new Class<?>[]{facadeClass}, new MethodInvocationHandler(delegator));
			} else{
				ProxyFactory f = new ProxyFactory();
				f.setSuperclass(facadeClass);
				f.setFilter(new MethodFilter() {
					public boolean isHandled(Method m) {
						// ignore finalize()
						return !m.getName().equals("finalize");
					}
				});
				Constructor[] all = facadeClass.getConstructors();
				Constructor candidate=null;
				for (Constructor c : all){
					if (candidate==null || c.getParameterTypes().length < candidate.getParameterTypes().length ) {
						candidate = c;
					}
				}
				Object[] allNull = new Object[candidate.getParameterTypes().length]; 
				return (T)f.create(candidate.getParameterTypes(), allNull, new MethodHandler(){

					@Override
					public Object invoke(Object proxy, Method invokedOnInterface, Method originalOnClass, Object[] args) throws Throwable {
						return delegator.invoke(proxy, invokedOnInterface, originalOnClass, args);
					}

					
				});

			} 
		} catch (InstantiationException e) {
			throw new InstantiationReflectionException(facadeClass.getName(), e.getMessage());
		} catch (Exception e) {
			throw new ReflectionExceptionHandler().handle(e);
		}
	}

	
	public static <I> I proxy (final Object delegationTarget , Class<I> proxyInterface){
		if (!proxyInterface.isInterface()){
			throw new IllegalArgumentException("Proxy must be applied with an interface");
		}
		
		return proxyInterface.cast(Proxy.newProxyInstance(delegationTarget.getClass().getClassLoader(), new Class[]{proxyInterface}, new InvocationHandler(){

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				try {
					return method.invoke(delegationTarget, args);  // execute the original method.
				} catch (IllegalArgumentException e){
					// try to find a method with the same name and parameters 
					Method m = delegationTarget.getClass().getMethod(method.getName(), method.getParameterTypes());
					if (m!=null && m.getReturnType().isAssignableFrom(method.getReturnType()) ){
						return m.invoke(delegationTarget, args);
					} else {
						throw new NoSuchMethodReflectionException (method.toString());
					}
				}
				
			}
			
		}));
	}

	public static <I> I proxy (Object delegationTarget , Class<I> proxyInterface , final ProxyHandler delegator ){
		if (!proxyInterface.isInterface()){
			throw new IllegalArgumentException("Proxy must be applied with an interface");
		}

		if (proxyInterface.isInstance(delegationTarget)){
			// the object already implements the interface. just wrapp it
			return proxyInterface.cast(Proxy.newProxyInstance(delegationTarget.getClass().getClassLoader(), new Class[]{proxyInterface}, new InvocationHandler(){

				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					return delegator.invoke(proxy, method, null, args);
				}
				
			}));
			
		} else {
			// delegationTarget does not implement the same interface

			ProxyFactory f = new ProxyFactory();
			f.setSuperclass(delegationTarget.getClass());
			f.setInterfaces(new Class[]{proxyInterface});
			f.setFilter(new MethodFilter() {
				public boolean isHandled(Method m) {
					// ignore finalize()
					return !m.getName().equals("finalize");
				}
			});

			I foo;
			try {
				Class c = f.createClass();
				foo = (I)c.newInstance();

				((ProxyObject)foo).setHandler(new MethodHandler(){

					@Override
					public Object invoke(Object proxy, Method invoked, Method original, Object[] args) throws Throwable {
						return delegator.invoke(proxy, invoked, original, args);
					}
					
				});

				copy(delegationTarget, foo);
				return foo;
			} catch (InstantiationException e) {
				throw new ReflectionException(e);
			} catch (IllegalAccessException e) {
				throw new ReflectionException(e);
			}
		}
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
		Collection<PropertyAccessor> assessors = getPropertyAccessors(original.getClass());

		for (PropertyAccessor fa : assessors){
			fa.setValue(copy, fa.getValue(original));
		}
		return copy;
	}

	public static PropertyAccessor getPropertyAccessor(Class<?> type, String fieldName){
		return new PropertyAccessor(type,fieldName);
	}

	public static Collection<PropertyAccessor> getPropertyAccessors(Class<?> type)throws ReflectionException{

		if (type==null){
			return Collections.emptySet();
		}
		
		Collection<PropertyAccessor> result = new ArrayList<PropertyAccessor> ();
		for (Method m : type.getMethods()){
			
			if (!m.getName().startsWith("getClass") ){
				if (m.getName().startsWith("get")){
					result.add(getPropertyAccessor(type, m.getName().substring(3)));
				} else if (m.getName().startsWith("is")){
					result.add(getPropertyAccessor(type, m.getName().substring(2)));
				}
				
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

	public static List<Method> getMethods(Class<?> type) {
		return getMethods(type,null);
	}
	
	public static List<Method> getMethods(Class<?> type, BooleanClassifier<Method> filter) {
		
		List<Method> result = new ArrayList<Method>(Arrays.asList(type.getMethods()));
		
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
