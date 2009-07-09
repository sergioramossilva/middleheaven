package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.middleheaven.core.reflection.ClassIntrospector;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.wiring.annotations.BindingSpecification;
import org.middleheaven.core.wiring.annotations.Name;
import org.middleheaven.core.wiring.annotations.Params;
import org.middleheaven.core.wiring.annotations.ScopeSpecification;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.Walker;

public class DefaultWiringModelParser extends AbstractAnnotationBasedWiringModelParser{


	private Class<? extends Annotation>[] annotations;

	public DefaultWiringModelParser(){
		this(Wire.class);
	}

	public DefaultWiringModelParser(Class<? extends Annotation> ... adicionalAnnotations){
		this.annotations = CollectionUtils.addToArray(adicionalAnnotations, Wire.class);
	}

	@Override
	public <T> void readWiringModel(Class<T> type, final WiringModel model) {
		// TODO implement WiringModelReader.readWiringModel

		ClassIntrospector<T> introspector = Introspector.of(type);
		
		if (model.getConstructorPoint()==null){
		
			// constructor
			EnhancedCollection<Constructor<T>> constructors = introspector.inspect()
			.constructors().annotathedWith(annotations).retriveAll();

			if (constructors.isEmpty()){
				// search all constructors
				constructors = introspector.inspect().constructors().retriveAll();
				if (constructors.size()>1){
					throw new ConfigurationException("Multiple constructors found for " + type + ". Annotate only one with @" + Wire.class.getSimpleName());
				}
			} else if (constructors.size()>1){
				throw new ConfigurationException("Only one constructor may be annotated with @" + Wire.class.getSimpleName());
			} 

			Constructor<T> constructor  = constructors.getFist();

			WiringSpecification[] params = readParamsSpecification(constructor, new BooleanClassifier<Annotation>(){

				@Override
				public Boolean classify(Annotation a) {
					return a.annotationType().equals(BindingSpecification.class) ||
					a.annotationType().equals(ScopeSpecification.class) ||
					a.annotationType().equals(Name.class) || 
					a.annotationType().equals(Params.class); 
				}

			});

			model.setConstructorPoint(new ConstructorWiringPoint(constructor,null,params));
		}

		// injection points

		introspector.inspect().fields().annotatedWith(annotations).retriveAll()
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
				
				model.addAfterWiringPoint(new FieldWiringPoint(f,spec));
			}
			
		} );

		introspector.inspect().methods().annotatedWith(annotations).retriveAll()
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
				model.addAfterWiringPoint(new MethodWiringPoint(method, null, params));
			}
			
		});



	}

	@Override
	public void readScoopingModel(Object obj, ScoopingModel model) {
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
