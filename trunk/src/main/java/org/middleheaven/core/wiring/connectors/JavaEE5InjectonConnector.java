package org.middleheaven.core.wiring.connectors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.wiring.AbstractAnnotationBasedWiringModelParser;
import org.middleheaven.core.wiring.ConnectableBinder;
import org.middleheaven.core.wiring.ConstructorWiringPoint;
import org.middleheaven.core.wiring.FieldWiringPoint;
import org.middleheaven.core.wiring.MethodWiringPoint;
import org.middleheaven.core.wiring.ScoopingModel;
import org.middleheaven.core.wiring.WiringConnector;
import org.middleheaven.core.wiring.WiringModel;
import org.middleheaven.core.wiring.WiringSpecification;
import org.middleheaven.core.wiring.namedirectory.NameDirectoryScope;
import org.middleheaven.util.classification.BooleanClassifier;

public class JavaEE5InjectonConnector implements WiringConnector {


	public JavaEE5InjectonConnector(){}

	@Override
	public void connect(ConnectableBinder binder) {

		binder.addWiringModelParser(new JavaEE5InjectonParser());
	}


	private static class JavaEE5InjectonParser extends AbstractAnnotationBasedWiringModelParser{

		@Override
		public <T> void readWiringModel(Class<T> type, WiringModel model) {

			//if (model.getConstructorPoint()==null){
				// constructor
				List<Constructor<T>> constructors = Introspector.of(type).inspect()
				.constructors().retrive().asList();
				
				if (constructors.size()==1){
					Constructor constructor = constructors.get(0);
					//ok, use this one
					WiringSpecification[] params = this.readParamsSpecification(constructor, new BooleanClassifier<Annotation>(){

						@Override
						public Boolean classify(Annotation a) {
							return a.annotationType().isAnnotationPresent(Resource.class);
						}
						
					});
					
					model.setConstructorPoint(new ConstructorWiringPoint(constructors.get(0),null,params));
				} else {
					// search for one with parameters annotated with @Resource or @Resources
					
					outer:for (Constructor<T> constructor : constructors){
						Annotation[][] paramsAnnotation = constructor.getParameterAnnotations();
						
						for (int p =0; p< paramsAnnotation.length;p++){
							// inner classes have a added parameter on index 0 that 
							// get annotations does not cover.
							// read from end to start

					 
							int annotIndex = paramsAnnotation.length - 1 -p;
							
							for (Annotation a : paramsAnnotation[annotIndex]){

								if (a.annotationType().isAnnotationPresent(Resource.class)){
									model.setConstructorPoint(new ConstructorWiringPoint(constructor,null,null));
									break outer;
								}
							}
							
						}
					}
				}


			//}


			// injection points

			Set<Field> fields = ReflectionUtils.allAnnotatedFields(type, Resource.class);

			for (Field f : fields){
				Set<Annotation> specs = ReflectionUtils.getAnnotations(f, Resource.class);
				
				WiringSpecification spec = readParamsSpecification(f, new BooleanClassifier<Annotation>(){

					@Override
					public Boolean classify(Annotation a) {
						return a.annotationType().isAnnotationPresent(Resource.class);
					}
				
				});
				
				model.addAfterWiringPoint(new FieldWiringPoint(f, spec));
			}

			Set<Method> methods = ReflectionUtils.allAnnotatedMethods(type, Resource.class);

			for (Method method : methods){
				WiringSpecification[] spec = readParamsSpecification(method, new BooleanClassifier<Annotation>(){

					@Override
					public Boolean classify(Annotation a) {
						return a.annotationType().equals(Resource.class);
					}
				
				});
				
				model.addAfterWiringPoint(new MethodWiringPoint(method,null,spec));
			}
		}

		@Override
		public void readScoopingModel(Object obj, ScoopingModel model) {

			Set<Annotation> all = ReflectionUtils.getAnnotations(obj.getClass());
			
			for (Annotation a : all){
				if (a.annotationType().isAnnotationPresent(Resource.class)){
					Resource resource = a.annotationType().getAnnotation(Resource.class);
					model.addScope(NameDirectoryScope.class);
					model.addParam("name", resource.name());
					model.addParam("shareable",Boolean.valueOf(resource.shareable()).toString());
					
				}
			}
			
		}

	}
}
