package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.annotations.BindingSpecification;
import org.middleheaven.core.wiring.annotations.Factory;
import org.middleheaven.core.wiring.annotations.Name;
import org.middleheaven.core.wiring.annotations.Params;
import org.middleheaven.core.wiring.annotations.ScopeSpecification;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.util.classification.BooleanClassifier;
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

	@Override
	public <T> void readWiringModel(Class<T> type, final WiringModel model) {

		ClassIntrospector<T> introspector = Introspector.of(type);
		
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
				WiringSpecification spec = readParamsSpecification(f, new BooleanClassifier<Annotation>(){

					@Override
					public Boolean classify(Annotation a) {
						return a.annotationType().equals(Wire.class) ||
						a.annotationType().equals(ScopeSpecification.class) ||
						a.annotationType().equals(Name.class) || 
						a.annotationType().equals(Params.class); 
					}

				});
				
				model.addAfterWiringPoint(new FieldAfterWiringPoint(f,spec));
			}
			
		} );

		introspector.inspect().methods().beingStatic(false).annotatedWith(annotations).retriveAll()
		.each(new Walker<Method>(){

			@Override
			public void doWith(Method method) {
				WiringSpecification[] params = readParamsSpecification(method, new BooleanClassifier<Annotation>(){

					@Override
					public Boolean classify(Annotation a) {
						return a.annotationType().equals(Wire.class) ||
						a.annotationType().equals(ScopeSpecification.class) ||
						a.annotationType().equals(Name.class) || 
						a.annotationType().equals(Params.class); 
					}

				});

				// TODO read method specification
				model.addAfterWiringPoint(new MethodAfterWiringPoint(method, null, params));
			}
			
		});



	}

	private <T> void readConstructorProducingPoint(Class<T> type,
			final WiringModel model, ClassIntrospector<T> introspector) {
		// constructor
		EnhancedCollection<Constructor<T>> constructors = introspector.inspect()
		.constructors().withAccess(MemberAccess.PUBLIC).annotathedWith(annotations).retriveAll();

		if (constructors.isEmpty()){
			// search all constructors
			constructors = introspector.inspect().constructors().withAccess(MemberAccess.PUBLIC).retriveAll();
			if (constructors.size()>1 && !Introspector.of(type).isEnhanced()){
				throw new ConfigurationException("Multiple constructors found for " + type + ". Annotate only one with @" + Wire.class.getSimpleName());
			} else if (constructors.isEmpty()) {
				// no public constructors.
				throw new ConfigurationException("No public constructors found for " + type + ". Maybe you want to use @" + Factory.class.getSimpleName());
			}
		} else if (constructors.size()>1){
			throw new ConfigurationException("Only one constructor may be annotated with @" + Wire.class.getSimpleName());
		} 

		Constructor<T> constructor  = constructors.getFist();

		if (constructor != null){
				WiringSpecification[] params = readParamsSpecification(constructor, new BooleanClassifier<Annotation>(){
	
				@Override
				public Boolean classify(Annotation a) {
					return a.annotationType().equals(BindingSpecification.class) ||
					a.annotationType().equals(ScopeSpecification.class) ||
					a.annotationType().equals(Name.class) || 
					a.annotationType().equals(Params.class); 
				}
	
			});
	
			model.setProducingWiringPoint(new ConstructorWiringPoint(constructor,null,params));
		} 
	}

	@Override
	public void readScoopingModel(Object obj, ScoopingModel model) {
		if (obj==null){
			return;
		}
		Collection<Annotation> annotations = Introspector.of(obj.getClass()).inspect().annotations().retrive();
		
		for (Annotation a: annotations){
			if (a.annotationType().isAnnotationPresent(ScopeSpecification.class)){
				model.addScope(a.annotationType());
			}else if (a.annotationType().isAnnotationPresent(Name.class)){
				Name name = a.annotationType().getAnnotation(Name.class);
				model.addParam("name", name.value());
			} else if (a.annotationType().isAnnotationPresent(Params.class)){
				Params name = a.annotationType().getAnnotation(Params.class);
				String[] paramPairs = name.value();
				for (String paramPair : paramPairs){
					String[] values = paramPair.split("=");
					if(values.length!=2){
						throw new IllegalStateException("Param pair expected to be in format name=value bu found" + paramPair);
					}
					model.addParam(values[0], values[1]);
				}
			}
		}
	}





}
