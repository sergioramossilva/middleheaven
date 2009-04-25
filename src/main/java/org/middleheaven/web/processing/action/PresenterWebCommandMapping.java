package org.middleheaven.web.processing.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.core.reflection.InvocationTargetReflectionException;
import org.middleheaven.core.reflection.MethodFilters;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.global.text.LocalizationService;
import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.logging.Logging;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.util.conversion.StringCalendarDateTimeConverter;
import org.middleheaven.util.conversion.StringDateConverter;
import org.middleheaven.util.conversion.TypeConvertions;
import org.middleheaven.validation.ValidationException;
import org.middleheaven.web.annotations.Delete;
import org.middleheaven.web.annotations.Get;
import org.middleheaven.web.annotations.In;
import org.middleheaven.web.annotations.Post;
import org.middleheaven.web.annotations.ProcessRequest;
import org.middleheaven.web.annotations.Put;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;

/**
 * Delegates action execution to a Presenter class
 */
public class PresenterWebCommandMapping implements WebCommandMapping {

	private final Class<?> controllerClass;
	private Object controllerObject;
	private final List<Interceptor> interceptors = new LinkedList<Interceptor>();
	private final Map<String, Map<OutcomeStatus, Outcome>> outcomes = new HashMap<String, Map<OutcomeStatus, Outcome>>();
	private final Map<String , Method> actions = new TreeMap<String,Method>();
	private final List<String> patterns = new LinkedList<String>();

	private EnumMap<HttpMethod, Method> serviceMethods = new EnumMap<HttpMethod, Method>(HttpMethod.class);

	private Method doService=null;

	public String toString(){
		return controllerClass.getName() + "->" + patterns.toString();
	}

	public PresenterWebCommandMapping(Class<?> presenterClass) {
		super();

		Map<OutcomeStatus, Outcome> actionOutcome = new HashMap<OutcomeStatus, Outcome>();
		this.outcomes.put(null,actionOutcome );

		for (OutcomeStatus status : BasicOutcomeStatus.values()){
			actionOutcome.put(status,new Outcome(status,500));
		}

		this.controllerClass = presenterClass;

		// each method on the presenter that is not a getter or a setter is an action
		// only public not property methods
		List<Method> methods = ReflectionUtils.getMethods(presenterClass , MethodFilters.publicInstanceNonProperty());

		for (Method m : methods ){

			// a method can have multiple bindings (not chain ifs)
			if (m.isAnnotationPresent(Post.class)){
				serviceMethods.put(HttpMethod.POST,m);
			} 
			if (m.isAnnotationPresent(Get.class)){
				serviceMethods.put(HttpMethod.GET,m);
			} 
			if (m.isAnnotationPresent(Delete.class)){
				serviceMethods.put(HttpMethod.DELETE,m);
			} 
			if (m.isAnnotationPresent(Put.class)){
				serviceMethods.put(HttpMethod.PUT,m);
			} 
			if (m.isAnnotationPresent(ProcessRequest.class)){
				doService = m;
			} else {
				actions.put(m.getName().toLowerCase(),m);
			}

		}

		// singleton per mapper
		controllerObject = ServiceRegistry.getService(WiringService.class).getWiringContext().getInstance(presenterClass);
	}

	public void addPathMatcher(String regex){
		patterns.add(regex);
	}

	@Override
	public boolean matches(CharSequence url) {
		for (String pattern : patterns){

			String dynamicUrl = url.toString();

			/*
			 * '*' is not a valid url 
			 */
			if(dynamicUrl.indexOf("*") >= 0){
				return false;
			}

			// verify match
			final int indexLeft = pattern.indexOf("*");		
			final int indexRight = pattern.lastIndexOf("*");	

			// has any *
			if (indexLeft>=0 || indexRight>=0){

				// is just a * an nothing else : match all
				if (pattern.length()==1){
					return true;
				}

				boolean found = false; 
				if (indexLeft == indexRight){ // one *
					if (indexLeft==0){ // is on the left *xxxx
						found = dynamicUrl.endsWith(pattern.substring(1));
					} else if (indexLeft == pattern.length()-1){ // is on the right xxxx*
						found =  dynamicUrl.startsWith(pattern.substring(0,indexLeft));
					} else { // is on the middle xxx*xx
						found = dynamicUrl.startsWith(pattern.substring(0, indexLeft)) && dynamicUrl.endsWith(pattern.substring(indexLeft+1));
					}
				} else if (indexLeft == 0 &&  indexRight == pattern.length() -1 ){ 
					found = dynamicUrl.contains(pattern.substring(indexLeft+1, indexRight));
				}  // false in any other case

				if (found){
					return true;
				} // else , try another pattern
			} else if (pattern.equals(url)){
				return true;
			}

		}
		return false;
	}

	public void addInterceptor(Interceptor interceptor) {
		interceptors.add(interceptor);
	}

	public void addOutcome(String action, Outcome outcome){
		Map<OutcomeStatus, Outcome> actionOutcome = this.outcomes.get(action);
		if (actionOutcome==null){
			actionOutcome = new HashMap<OutcomeStatus, Outcome>();
			this.outcomes.put(action,actionOutcome);
		}
		actionOutcome.put(outcome.getStatus(), outcome);
	}

	private Outcome resolveOutcome(String action, OutcomeStatus status){
		Map<OutcomeStatus, Outcome> actionOutcome = this.outcomes.get(action);
		if (actionOutcome==null || status.equals(BasicOutcomeStatus.TERMINATE)){
			return new TerminalOutcome();
		} else {
			return actionOutcome.get(status);
		}
	}

	@Override
	public Outcome execute(HttpContext context) {

		ListInterceptorChain chain = new ListInterceptorChain(this.interceptors){

			public Outcome doFinal(HttpContext context){
				return executeAction(context);
			}

			protected Outcome resolveOutcome (OutcomeStatus status){
				return PresenterWebCommandMapping.this.resolveOutcome(null,status);
			}
		};

		chain.doChain(context);

		return chain.getOutcome();
	}

	public Outcome executeAction(HttpContext context){ 
		Outcome outcome;

		Method actionMethod=null;
		String action = null;
		try {
			// Determine action using different stategies

			// try the name match from url
			String actionNameFromURL = context.getAttribute(ContextScope.REQUEST, "action", String.class);
			actionMethod = actionNameFromURL==null ? null : actions.get(actionNameFromURL);

			if (actionMethod==null){
				// try the name match if there are any parameters
				if (!context.getParameters().isEmpty()){
					for ( Map.Entry<String,Method> entry : actions.entrySet()){
						String act = context.getAttribute(ContextScope.PARAMETERS, entry.getKey(), String.class);
						if (act!=null){
							actionMethod = entry.getValue();
							break;
						}
					}
				}
				if (actionMethod==null){
					// try the service match by type of request
					actionMethod = serviceMethods.get(context.getHttpService());
				}

				if (actionMethod==null && doService!=null){ 
					// try generic service
					actionMethod = doService; 
				} 

				if (actionMethod==null){
					if (context.getHttpService().equals(HttpMethod.GET)){
						return resolveOutcome(null,BasicOutcomeStatus.SUCCESS);
					} else {
						throw new ActionHandlerNotFoundException();
					}
				}
			}
			action = actionMethod.getName().toLowerCase();

			Object[] args = inicilizeActionParameters(actionMethod, context);


			Object result = ReflectionUtils.invoke(actionMethod.getReturnType(), actionMethod, controllerObject, args);

			if (result instanceof Outcome){
				if (result instanceof URLOutcome ){
					return (Outcome)result;
				} else {
					Logging.error("Illegal outcome class. Use URLOutcome.");
					outcome =  resolveOutcome(action,BasicOutcomeStatus.FAILURE);
				}
			}else if (result==null || !(result instanceof OutcomeStatus)){
				return resolveOutcome(action,BasicOutcomeStatus.SUCCESS);
			} else {
				return resolveOutcome(action,(OutcomeStatus)result);
			}
		} catch (ValidationException e){
			outcome =  resolveOutcome(action,BasicOutcomeStatus.INVALID);
		} catch (InvocationTargetReflectionException e){
			Logging.error("Exception found invoking " + actionMethod.getName(), e);
			context.setAttribute(ContextScope.REQUEST, "exception", e.getCause());
			outcome =  resolveOutcome(action,BasicOutcomeStatus.FAILURE);
		}catch (ActionHandlerNotFoundException e){
			Logging.fatal("Action not found", e);
			outcome =  resolveOutcome(action,BasicOutcomeStatus.ERROR);
		}catch (Exception e){
			Logging.error("Exception found handling request", e);
			context.setAttribute(ContextScope.REQUEST, "exception", e);
			outcome =  resolveOutcome(action,BasicOutcomeStatus.FAILURE);
		} catch (Error e){
			Logging.fatal("Exception found handling request", e);
			context.setAttribute(ContextScope.REQUEST, "exception", e);
			outcome =  resolveOutcome(action,BasicOutcomeStatus.ERROR);
			if (outcome==null){
				outcome =  resolveOutcome(action,BasicOutcomeStatus.FAILURE);
			}
		}
		return outcome;
	}

	private Object[] inicilizeActionParameters(Method action,HttpContext context){
		Class<?>[] argClasses = action.getParameterTypes();
		Object[] args = new Object[argClasses.length];

		for (int i =0; i <argClasses.length; i++ ){
			if (argClasses[i].isAssignableFrom(WebContext.class)){
				args[i] = context;
			} else if (argClasses[i].isAssignableFrom(HttpServletRequest.class)){
				if (context instanceof RequestResponseWebContext){
					args[i] = ((RequestResponseWebContext)context).getRequest();
				} else {
					throw new IllegalStateException("Is not possible to inject " + HttpServletRequest.class.getName() + " on current environment");
				}
			} else if (argClasses[i].isAssignableFrom(HttpServletResponse.class)){
				if (context instanceof RequestResponseWebContext){
					args[i] = ((RequestResponseWebContext)context).getResponse();
				} else {
					throw new IllegalStateException("Is not possible to inject " + HttpServletResponse.class.getName() + " on current environment");
				}
			} else if (!argClasses[i].isPrimitive()){
				args[i]=loadBean(context,argClasses[i], action.getParameterAnnotations()[i]);
			}
		}

		return args;
	}

	private <A extends Annotation> A getAnnotation(Annotation[] annotations ,Class<A> annotationClass){
		for (Annotation a : annotations){
			if(annotationClass.isAssignableFrom(a.getClass())){
				return annotationClass.cast(a);
			}
		}
		return null;
	}
	
	private <T> T loadBean(HttpContext context, Class<T> type ,Annotation[] annotations) {
		try{
			// set up timestamp formatters and converter
			LocalizationService i18nService = ServiceRegistry.getService(LocalizationService.class);

			TimepointFormatter formatter = i18nService.getTimestampFormatter(context.getCulture());
			
			formatter.setPattern(TimepointFormatter.Format.DATE_ONLY);
			
			TypeConvertions.addConverter(String.class, Date.class, new StringDateConverter(formatter));
			TypeConvertions.addConverter(String.class, CalendarDateTime.class, new StringCalendarDateTimeConverter(formatter));

			T object = null;
			
			String name = "";
			ContextScope inScope =null;
			In in = getAnnotation(annotations,In.class);
			if (in!=null){
				name = in.value();
				inScope = in.scope();
				if(name.isEmpty()){
					throw new IllegalArgumentException("Cannot inject an unamed parameter");
				}
				return context.getAttribute(inScope, name, type);

			} else {
				if (isPrimitive(object)){
					throw new IllegalArgumentException("Use @In for instanceof " + object.getClass());
				}

				// search in the contexts in reverse order to find an object of class type
				ContextScope[] scopes = new ContextScope[]{
						ContextScope.APPLICATION,
						ContextScope.SESSION,
						ContextScope.REQUEST};


				for (ContextScope scope : scopes){
					Enumeration<String> names = context.getAttributeNames(scope);
					while (names.hasMoreElements()){
						Object candidate = context.getAttribute(scope, names.nextElement(), null);
						if (candidate!=null && type.isAssignableFrom(candidate.getClass())){
							object = type.cast(candidate);
							break;
						}
					}
				}

				if (object==null){

					// try to load from parameters
					object =  new ContextAssembler(context, ContextScope.PARAMETERS).assemble(type);
				}
			}
			return object;
		} finally {
			TypeConvertions.removeConverter(String.class, Date.class);
			TypeConvertions.removeConverter(String.class, CalendarDateTime.class);

		}



	}

	private boolean isPrimitive(Object obj){
		return obj!=null && (obj.getClass().isPrimitive() || 
				obj instanceof String || 
				obj instanceof Date || 
				obj instanceof Number);
	}








}
