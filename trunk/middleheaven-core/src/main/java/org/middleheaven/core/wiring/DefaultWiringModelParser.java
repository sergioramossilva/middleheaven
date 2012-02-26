package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.taskdefs.XSLTProcess.Param;
import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.activation.MethodCallPoint;
import org.middleheaven.core.wiring.activation.MethodPublishPoint;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Default;
import org.middleheaven.core.wiring.annotations.Factory;
import org.middleheaven.core.wiring.annotations.Named;
import org.middleheaven.core.wiring.annotations.Params;
import org.middleheaven.core.wiring.annotations.PostCreate;
import org.middleheaven.core.wiring.annotations.PreDestroy;
import org.middleheaven.core.wiring.annotations.Qualifier;
import org.middleheaven.core.wiring.annotations.ScopeSpecification;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.Walker;


/**
 * The default MiddleHeaven wiring model parser.
 * 
 * Wiring is requested by using the @{@link Wire} annotation.
 * The type of the wiring point will be used to discover witch object to use. The object of the same type will be used by default.
 * 
 * @{@link Name} is used to differenced (to name) the object that is required.
 * 
 * @{@link Params} and @{@link Param} can be used to parameterize the wiring.
 * 
 */
public class DefaultWiringModelParser extends AbstractAnnotationBasedWiringModelParser{


	private Class<? extends Annotation>[] annotations;

	/**
	 * 
	 * Constructor.
	 */
	@SuppressWarnings("unchecked")
	public DefaultWiringModelParser(){
		this.annotations = new Class[]{Wire.class};
	}

	/**
	 * 
	 * Constructor.
	 * @param adicionalAnnotations other annotations that can have a semantic identity with @{@link Wire}.
	 */
	public DefaultWiringModelParser(Class<? extends Annotation> ... adicionalAnnotations){
		if (!CollectionUtils.arrayContains(adicionalAnnotations, Wire.class)){
			this.annotations = CollectionUtils.appendToArrayEnd(adicionalAnnotations, Wire.class);
		} else {
			this.annotations = adicionalAnnotations;
		}

	}


	protected void parseSpecialAnnotations(Annotation a, Map<String, Object> params ){

		if (a.annotationType().isAnnotationPresent(Params.class)){
			Params name = a.annotationType().getAnnotation(Params.class);
			String[] paramPairs = name.value();
			for (String paramPair : paramPairs){
				String[] values = paramPair.split("=");
				if(values.length != 2){
					throw new IllegalStateException("Param pair expected to be in format name=value butt found" + paramPair);
				}
				params.put(values[0], values[1]);
			}
		} else if (Named.class.isAssignableFrom(a.annotationType())){
			params.put("name", ((Named)a).value());
		}

		processQualifier(a, params);
	}


	@Override
	public <T> void readBeanModel(Class<T> type, final BeanModel model) {

		ClassIntrospector<T> introspector = Introspector.of(type);

		final Class<?>[] interfaces = Introspector.of(type).getDeclaredInterfaces();

		for (Annotation a : introspector.getAnnotations()){

			if (a.annotationType().isAnnotationPresent(ScopeSpecification.class)){
				model.addScope(a.annotationType());
				
				if (a.annotationType().equals(Service.class)){
					if (interfaces.length == 0){
						throw new IllegalStateException("A @Service must be declared on an annotation or the type must implement a interface");
					}
					model.setContractType(interfaces[0]);
				}
			}

			this.parseSpecialAnnotations(a, model.getParams());
		}

		if(model.getScopes().isEmpty()){
			for (Class i : interfaces){
				for (Annotation a : i.getAnnotations()){

					if (a.annotationType().isAnnotationPresent(ScopeSpecification.class)){
						model.addScope(a.annotationType());
						model.setContractType(i);
					}

				}
			}
		}

		if(model.getScopes().isEmpty()){
			model.addScope(Default.class);
		}

		// find publish points


		Collection<Method> methods =  introspector.inspect().methods().notInheritFromObject().beingStatic(false).searchHierarchy().retriveAll();

		for (Method method : methods){

			if (method.isAnnotationPresent(Publish.class)){
				if (method.getParameterTypes().length!=0){
					throw new ConfigurationException("@" + Publish.class.getSimpleName() + " cannot be used on a method with parameters");
				}

				Publish p = method.getAnnotation(Publish.class);

				// process params. this params will be added to the published bean model.
				String[] paramPairs = p.value();

				Map<String,Object> params = new HashMap<String,Object> ();

				for (String paramPair : paramPairs){
					String[] values = paramPair.split("=");
					if(values.length!=2){
						throw new IllegalStateException("Param pair expected to be in format name=value but found" + paramPair);
					}
					params.put(values[0], values[1]);
				}
				model.addPublishPoint(new MethodPublishPoint(method, params));
			} else if (method.isAnnotationPresent(PostCreate.class)){
				model.setPostCreatePoint(new MethodCallPoint(method));
			} else if (method.isAnnotationPresent(PreDestroy.class)){
				model.setPreDestroiPoint(new MethodCallPoint(method));
			}


		}



		if (model.getProducingWiringPoint() == null && !type.isInterface()){ // only create the producing point if none is already readed.


			// read @Factory

			Collection<Method> candidates = introspector.inspect().methods().beingStatic(true).annotatedWith(Factory.class).retriveAll();

			if (candidates.isEmpty()) {
				readConstructorProducingPoint(type, model, introspector);
			} else if ( candidates.size() > 1){
				throw new ConfigurationException("Multiple static factory methods found for " + type + ". Annotate only one method with @" + Factory.class.getSimpleName());
			} else {

			}
		}

		// injection points

		introspector.inspect().fields().beingStatic(false).annotatedWith(annotations).retriveAll()
		.each(new Walker<Field>(){

			@Override
			public void doWith(Field f) {
				model.addAfterWiringPoint(new FieldAfterWiringPoint(f,readParamsSpecification(f)));
			}

		} );

		introspector.inspect().methods().beingStatic(false).annotatedWith(annotations).retriveAll()
		.each(new Walker<Method>(){

			@Override
			public void doWith(Method method) {
				model.addAfterWiringPoint(new MethodAfterWiringPoint(method, null, readParamsSpecification(method)));
			}

		});



	}

	private <T> void readConstructorProducingPoint(Class<T> type,
			final BeanModel model, ClassIntrospector<T> introspector) {
		// constructor
		EnhancedCollection<Constructor<T>> constructors = introspector.inspect()
				.constructors().withAccess(MemberAccess.PUBLIC).annotathedWith(annotations).retriveAll();

		if (constructors.isEmpty()){
			// search all constructors
			constructors = introspector.inspect().constructors().withAccess(MemberAccess.PUBLIC).retriveAll();
			if (constructors.size()>1 && !Introspector.of(type).isEnhanced()){
				throw new ConfigurationException("Multiple constructors found for " + type + ". Annotate only one with @" + Wire.class.getSimpleName());
			} 
		} else if (constructors.size()>1){
			throw new ConfigurationException("Only one constructor may be annotated with @" + Wire.class.getSimpleName());
		} 

		Constructor<T> constructor  = constructors.getFirst();

		if (constructor != null){
			model.setProducingWiringPoint(new ConstructorWiringPoint(constructor,null,readParamsSpecification(constructor)));
		} 
	}


	/**
	 * @param annotationType
	 * @param params
	 */
	private void processQualifier(Annotation annotation, Map<String, Object> params) {
		processQualifier(annotation,params, new HashSet());
	}

	private void processQualifier(Annotation a, Map<String, Object> params, Set<Class<?>> visited) {

		if (a.annotationType().isAnnotationPresent(Qualifier.class)){

			Qualifier q = a.annotationType().getAnnotation(Qualifier.class);

			String quality = ((Qualifier) q).value();
			if (quality.isEmpty()){
				quality = a.annotationType().getName();
			}
			params.put(quality, a);

		} else if (visited.add(a.annotationType())){
			for (Annotation b : a.annotationType().getAnnotations()){
				processQualifier(b, params, visited);
			}
		}

	}

	protected WiringSpecification parseAnnotations(Annotation[] annnnotations ,Class<?> type){


		boolean isParamRequired = true;
		boolean isParamShareable = true;

		Map<String, Object> params = new HashMap<String, Object>();

		for (Annotation a : annnnotations){

			if (Wire.class.isAssignableFrom(a.annotationType())){
				Wire wire = (Wire)a;
				isParamRequired = wire.required();
				isParamShareable = wire.shareable();
			}


			parseSpecialAnnotations(a, params);

		}

		WiringSpecification spec = WiringSpecification.search(type,params);

		spec.setRequired(isParamRequired);
		spec.setShareable(isParamShareable);

		return spec;
	}



}
