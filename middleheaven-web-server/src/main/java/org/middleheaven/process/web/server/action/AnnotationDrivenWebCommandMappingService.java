/**
 * 
 */
package org.middleheaven.process.web.server.action;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.logging.Logger;
import org.middleheaven.process.web.server.action.annotaions.Interceptable;
import org.middleheaven.process.web.server.action.annotaions.Presenter;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.web.annotations.Path;
import org.middleheaven.web.annotations.Paths;



/**
 * 
 */
public class AnnotationDrivenWebCommandMappingService extends BuildableWebCommandMappingService {


	private WiringService wiringService;

	public AnnotationDrivenWebCommandMappingService (WiringService wiringService){
		this.wiringService = wiringService;
	}

	public void setObjectPool(WiringService wiringService) {
		this.wiringService = wiringService;
	}


	public PresenterCommandMappingBuilder map(final Class<?> presenter){

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				Class c = presenter;
				binder.bind(presenter).inSharedScope().to(c);
			}

		});

		return super.map(presenter);
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
			Enumerable<Method> methods = introspector.inspect().methods().notInheritFromObject().withAccess(MemberAccess.PUBLIC).retriveAll();

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

		Interceptable interceptable = introspector.getAnnotation(Interceptable.class);

		if (interceptable != null){
			for (Class<? extends ActionInterceptor> type : interceptable.value()){

				final ActionInterceptor instance = this.wiringService.getInstance(type);
				if (instance != null){
					urlMappingBuilder.through(instance);
				} else {
					Logger.onBookFor(this.getClass()).warn("Instance not found for interceptor {0}", type.getName());
				}
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

	/**
	 * @param presentersSet
	 */
	public void addPresenters(ClassSet presentersSet) {
		for (Class<?> type : presentersSet){
			add(type);
		}
	}
}
