package org.middleheaven.web;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.logging.Logging;
import org.middleheaven.ui.Context;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.validation.ValidationException;
import org.middleheaven.web.annotations.Delete;
import org.middleheaven.web.annotations.Get;
import org.middleheaven.web.annotations.Post;
import org.middleheaven.web.annotations.Put;
import org.middleheaven.web.annotations.Service;

/**
 * Delegates action execution to a Presenter class
 */
public class PresenterWebCommandMapping implements WebCommandMapping {

	private Class<?> presenterClass;
	private List<Interceptor> interceptors = new LinkedList<Interceptor>();
	private Map<OutcomeStatus, Outcome> outcomes = new HashMap<OutcomeStatus, Outcome>();
	private Map<String , Method> actions = new TreeMap<String,Method>();
	private List<String> patterns = new LinkedList<String>();

	private EnumMap<HttpServices, Method> serviceMethods = new EnumMap<HttpServices, Method>(HttpServices.class);

	private Method doService=null;


	public PresenterWebCommandMapping(Class<?> presenterClass) {
		super();
		
		outcomes.put(OutcomeStatus.TERMINATE, new TerminalOutcome());
		this.presenterClass = presenterClass;

		// each method on the presenter that is not a getter or a setter is an action
		Method[] methods = presenterClass.getMethods();
		for (Method m : methods ){
			// only public not property methods
			if ( !Modifier.isPublic(m.getModifiers()) || 
					m.getName().startsWith("set") || 
					m.getName().startsWith("get") || 
					m.getName().startsWith("is")){
				continue;
			}
			if (m.getName().equals("doPost") || m.isAnnotationPresent(Post.class)){
				serviceMethods.put(HttpServices.POST,m);
			} 
			if (m.getName().equals("doGet") || m.isAnnotationPresent(Get.class)){
				serviceMethods.put(HttpServices.GET,m);
			} 
			if (m.getName().equals("doDelete") || m.isAnnotationPresent(Delete.class)){
				serviceMethods.put(HttpServices.DELETE,m);
			} 
			if (m.getName().equals("doPut") || m.isAnnotationPresent(Put.class)){
				serviceMethods.put(HttpServices.PUT,m);
			} 
			if (m.getName().equals("doService") || m.isAnnotationPresent(Service.class)){
				doService = m;
			} else {
				actions.put(m.getName().toLowerCase(),m);
			}

		}
	}

	public void addPathMatcher(String regex){
		patterns.add(regex);
	}

	@Override
	public boolean matches(CharSequence url) {
		for (String p : patterns){
			if (p.equals(url)){
				return true;
			}
		}
		return false;
	}

	public void addInterceptor(Interceptor interceptor) {
		interceptors.add(interceptor);
	}

	public void addOutcome(Outcome outcome){
		outcomes.put(outcome.getStatus(), outcome);
	}

	@Override
	public Outcome execute(WebContext context) {

		MyChainListener listener = new MyChainListener();

		ListInterceptorChain chain = new ListInterceptorChain(this.interceptors, listener);
		chain.doChain(context);

		return listener.getOutcome();
	}

	private class MyChainListener implements ChainListener {

		// the chain may be broken before arriving to the presenter
		private Outcome outcome = new TerminalOutcome();

		@Override
		public void doFinal(WebContext context) {
			this.outcome = executeAction(context);
		}

		public Outcome getOutcome() {
			return outcome;
		}

	}

	public Outcome executeAction(WebContext context){ 
		// Determine action
		Method action=null;
		// try the name match from url
		String actionNameFromURL = context.getAttribute(ContextScope.REQUEST, "action", String.class);
		action = actions.get(actionNameFromURL);

		if (action==null){
			// try the name match
			for ( Map.Entry<String,Method> entry : actions.entrySet()){
				String act = context.getAttribute(ContextScope.PARAMETERS, entry.getKey(), String.class);
				if (act!=null){
					action = entry.getValue();
					break;
				}
			}
			// try the service match by type of request

			action = serviceMethods.get(context.getHttpService());

			if (action!=null && doService!=null){ // try generic service
				action = doService; 
			} 

			if (action==null){
				throw new ActionHandlerNotFoundException();
			}
		}

		Class<?>[] argClasses = action.getParameterTypes();
		Object[] args = new Object[argClasses.length];

		for (int i =0; i <argClasses.length; i++ ){
			if (argClasses[i].isAssignableFrom(WebContext.class)){
				args[i] = context;
			} else if (argClasses[i].isAssignableFrom(HttpServletRequest.class)){
				args[i] = context.getRequest();
			} else if (argClasses[i].isAssignableFrom(HttpServletResponse.class)){
				args[i] = context.getResponse();
			} else if (!argClasses[i].isPrimitive()){
				args[i]=loadBean(context,argClasses[i]);
			}
		}

		Outcome outcome;
		try {
			// create presenter
			final Object presenter = ReflectionUtils.newInstance(presenterClass);

			Object result = ReflectionUtils.invoke(action.getReturnType(), action, presenter, args);

			if (result==null){
				return outcomes.get(OutcomeStatus.SUCCESS);
			} else {
				return outcomes.get((OutcomeStatus)result);
			}
		} catch (ValidationException e){
			outcome =  outcomes.get(OutcomeStatus.INVALID);
		} catch (InvocationTargetReflectionException e){
			Logging.getBook("web").error("Exception found invoking " + action.getName(), e);
			context.setAttribute(ContextScope.REQUEST, "exception", e.getCause());
			outcome =  outcomes.get(OutcomeStatus.FAILURE);
		} catch (Exception e){
			Logging.getBook("web").error("Exception found handling request", e);
			context.setAttribute(ContextScope.REQUEST, "exception", e);
			outcome =  outcomes.get(OutcomeStatus.FAILURE);
		} catch (Error e){
			Logging.getBook("web").fatal("Exception found handling request", e);
			context.setAttribute(ContextScope.REQUEST, "exception", e);
			outcome =  outcomes.get(OutcomeStatus.ERROR);
			if (outcome==null){
				outcome =  outcomes.get(OutcomeStatus.FAILURE);
			}
		}
		return outcome;
	}

	private <T> T loadBean(Context context, Class<T> type) {

		// search in the contexts in reverse order to find an object of class type
		ContextScope[] scopes = new ContextScope[]{
				ContextScope.APPLICATION,
				ContextScope.SESSION,
				ContextScope.REQUEST};

		T object = null;
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
		return object;

	}










}
