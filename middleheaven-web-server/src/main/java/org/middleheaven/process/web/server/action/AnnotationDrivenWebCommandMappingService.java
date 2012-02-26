/**
 * 
 */
package org.middleheaven.process.web.server.action;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.ObjectPool;
import org.middleheaven.process.web.server.action.annotaions.Interceptor;
import org.middleheaven.process.web.server.action.annotaions.Interceptors;
import org.middleheaven.process.web.server.action.annotaions.Presenter;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.web.annotations.Path;
import org.middleheaven.web.annotations.Paths;



/**
 * 
 */
public class AnnotationDrivenWebCommandMappingService extends BuildableWebCommandMappingService {


	private ObjectPool objectPool;

	public AnnotationDrivenWebCommandMappingService (ObjectPool objectPool){
		this.objectPool = objectPool;
	}
	
	/**
	 * Atributes {@link ObjectPool}.
	 * @param objectPool the objectPool to set
	 */
	public void setObjectPool(ObjectPool objectPool) {
		this.objectPool = objectPool;
	}


	public void scan(ClassSet classSet){

		for (Class<?> type : classSet){
			add(type);
		}
	}


	/**
	 * Adds a presenter class. 
	 * The class must be annotated with @Presenter or implement ActionPresenter
	 * @param type
	 */
	private void add(Class<?> type){

		final ClassIntrospector<?> introspector = Introspector.of(type);

		if (introspector.isAnnotadedWith(Presenter.class)){

		
			String rootPath = readPath(introspector);




			//			.through(menuInterceptor)
			//			.through(breadcrumsInterceptor)
			//			
			EnhancedCollection<Method> methods = introspector.inspect().methods().notInheritFromObject().withAccess(MemberAccess.PUBLIC).retriveAll();

			for (Method m : methods){

				Paths paths = m.getAnnotation(Paths.class);

				Path[] all;
				if (paths!= null){
					all = paths.value();
				} else {
					Path p = m.getAnnotation(Path.class);
					if (p != null){
						all = new Path[]{p};
					} else {
						all = new Path[0];
					}
				}


				if (all.length == 0){
					PresenterCommandMappingBuilder builder = this.map(type);
					
					final URLMappingBuilder urlMappingBuilder = builder.to(resolvePath(rootPath, null));
				
					urlMappingBuilder.withAction(m.getName()).onSuccess().forwardTo(m.getName());
					
					setupInterceptors(urlMappingBuilder, introspector);
					
					this.addWebMapping(builder.getMapping());
				} else {

					for (Path p : all){

						PresenterCommandMappingBuilder builder = this.map(type);
						
						final URLMappingBuilder urlMappingBuilder = builder.to(resolvePath(rootPath, p.value()));
						
						String target;
						if (p.value().contains("*") || p.value().contains("{")){
							target = m.getName();
						} else {
							target = p.value();
						}
						
						urlMappingBuilder.withAction(m.getName()).onSuccess().forwardTo(target);
						
						setupInterceptors(urlMappingBuilder, introspector);
						
						
						this.addWebMapping(builder.getMapping());
					}
				}

			}

		} else if (introspector.isSubtypeOf(ActionPresenter.class)){

		}
	}
	
	public String resolvePath(String rootPath, String path ){
		if (path == null){
			return rootPath;
		} else {
			String s = StringUtils.concatIfNotEmptyPrefix( rootPath ,path, "/");
			while (s.contains("//")){
				s = s.replaceAll("//", "/");
			}
			return s;
		}
	}

	/**
	 * @param urlMappingBuilder
	 * @param introspector
	 */
	private void setupInterceptors(URLMappingBuilder urlMappingBuilder,
			ClassIntrospector<?> introspector) {
		
		Interceptors inteceptors = introspector.getAnnotation(Interceptors.class);

		Interceptor[] all;
		if (inteceptors != null){
			all = inteceptors.value();
		} else {
			Interceptor interceptor = introspector.getAnnotation(Interceptor.class);
			if (interceptor != null){
				all = new Interceptor[]{interceptor};
			} else {
				all = new Interceptor[0];
			}
		}

		
		for (Interceptor it : all){
			
			final ActionInterceptor instance = objectPool.getInstance(it.value());
			if (instance != null){
				urlMappingBuilder.through(instance);
			} else {
				org.middleheaven.logging.Log.onBookFor(this.getClass()).warn("Instance not found for interceptor {0}", it.value().getName());
			}
		}
		
	}


	/**
	 * @param introspector
	 * @return
	 */
	private String readPath(ClassIntrospector<?> introspector) {

		Path path = introspector.getAnnotation(Path.class);

		if ( path != null){
			return path.value();
		} else {
			return "";
		}

	}
}
