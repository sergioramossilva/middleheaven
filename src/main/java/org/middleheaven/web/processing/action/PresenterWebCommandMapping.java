package org.middleheaven.web.processing.action;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.InvocationTargetReflectionException;
import org.middleheaven.core.reflection.MethodFilters;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.logging.Log;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.util.collections.Walker;
import org.middleheaven.validation.ValidationException;
import org.middleheaven.web.annotations.Delete;
import org.middleheaven.web.annotations.Get;
import org.middleheaven.web.annotations.Post;
import org.middleheaven.web.annotations.ProcessRequest;
import org.middleheaven.web.annotations.Put;
import org.middleheaven.web.processing.HttpCode;
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
	private final AttributeContextBeanLoader beanLoader = new AttributeContextBeanLoader();
	
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
			actionOutcome.put(status,new Outcome(status,HttpCode.NOT_IMPLEMENTED));
		}

		this.controllerClass = presenterClass;

		// each method on the presenter that is not a getter or a setter is an action
		// only public not property methods
		Introspector.of(presenterClass).inspect().methods().match(MethodFilters.publicInstanceNonProperty())
		.each(new Walker<Method>(){

			@Override
			public void doWith(Method m) {
				// a method can have multiple bindings 
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
			
		});

		// singleton per mapper
		controllerObject = ServiceRegistry.getService(WiringService.class).getObjectPool().getInstance(presenterClass);
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
			// Determine action using different strategies

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
						throw new ActionHandlerNotFoundException(context.getHttpService() , this.controllerClass);
					}
				}
			}
			action = actionMethod.getName().toLowerCase();

			Object[] args = inicilizeActionParameters(actionMethod, context);


			Object result = Introspector.of(actionMethod).invoke(actionMethod.getReturnType(), controllerObject, args);

			if (result instanceof Outcome){
				if (result instanceof URLOutcome ){
					return (Outcome)result;
				} else {
					Log.onBookFor(this.getClass()).warn("Illegal outcome class. Use URLOutcome.");
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
			Log.onBookFor(this.getClass()).error(e,"Exception found invoking {0}", actionMethod.getName());
			context.setAttribute(ContextScope.REQUEST, "exception", e.getCause());
			outcome =  resolveOutcome(action,BasicOutcomeStatus.FAILURE);
		}catch (ActionHandlerNotFoundException e){
			Log.onBookFor(this.getClass()).fatal(e,"Action not found");
			outcome =  resolveOutcome(action,BasicOutcomeStatus.ERROR);
		}catch (Exception e){
			Log.onBookFor(this.getClass()).error(e,"Exception found handling request");
			context.setAttribute(ContextScope.REQUEST, "exception", e);
			outcome =  resolveOutcome(action,BasicOutcomeStatus.FAILURE);
		} catch (Error e){
			Log.onBookFor(this.getClass()).fatal(e,"Exception found handling request");
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
				args[i]=beanLoader.loadBean(context,argClasses[i], action.getParameterAnnotations()[i]);
			}
		}

		return args;
	}


	



	void addInterceptor(Interceptor interceptor) {
		this.interceptors.add(interceptor);
	}









}
