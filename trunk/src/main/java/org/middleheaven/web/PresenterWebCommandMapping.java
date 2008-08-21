package org.middleheaven.web;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.middleheaven.core.reflection.ReflectionUtils;

public class PresenterWebCommandMapping implements WebCommandMapping {

	
	private Class<?> presenterClass;
	private List<Interceptor> interceptors = new LinkedList<Interceptor>();
	private Map<OutcomeStatus, Outcome> outcomes = new HashMap<OutcomeStatus, Outcome>();
	private List<Method> actions = new LinkedList<Method>();
	private List<Pattern> patterns = new LinkedList<Pattern>();
	
	public PresenterWebCommandMapping(Class<?> presenterClass) {
		super();
		this.presenterClass = presenterClass;
		
		// each method on the presenter that is not a getter or a setter is an action
		Method[] methods = presenterClass.getMethods();
		for (Method m : methods ){
			if ( !Modifier.isPublic(m.getModifiers()) || 
					m.getName().startsWith("set") || 
					m.getName().startsWith("get") || 
					m.getName().startsWith("is")){
				continue;
			}
			actions.add(m);
		}
		
	}

	public void addPathMatcher(String regex){
		patterns.add(Pattern.compile(regex));
	}
	
	@Override
	public boolean matches(CharSequence url) {
		for (Pattern p : patterns){
			if (p.matcher(url).matches()){
				return true;
			}
		}
		return false;
	}
	
	public void addInterceptor(Interceptor interceptor) {
		interceptors.add(interceptor);
	}

	@Override
	public List<Interceptor> interceptors() {
		return interceptors;
	}
	
	public void addOutcome(Outcome outcome){
		outcomes.put(outcome.getStatus(), outcome);
	}
	
	@Override
	public Outcome execute(WebContext context) {
		
		// Determine action
		Method action=null;
		for ( Method m : actions){
			String act = context.getAttribute(ContextScope.PARAMETERS, m.getName().toLowerCase(), String.class);
			if (act!=null){
				action = m;
				break;
			}
		}
		Class<?>[] argClasses = action.getParameterTypes();
		Object[] args = new Object[argClasses.length];
		
		for (int i =0; i <argClasses.length; i++ ){
			if (argClasses[i].isAssignableFrom(WebContext.class)){
				args[i] = context;
			} else if (!argClasses[i].isPrimitive()){
				args[i]=loadBean(context,argClasses[i]);
			}
		}
		
		Outcome outcome;
		try {
			// create presenter
			final Object presenter = ReflectionUtils.newInstance(presenterClass);
	
			ReflectionUtils.invoke(action.getReturnType(), action, presenter, args);
			return outcomes.get(OutcomeStatus.SUCCESS);
		} catch (Exception e){
			outcome =  outcomes.get(OutcomeStatus.FAILURE);
		} catch (Error e){
			outcome =  outcomes.get(OutcomeStatus.ERROR);
			if (outcome==null){
				outcome =  outcomes.get(OutcomeStatus.FAILURE);
			}
		}
		return outcome;
	}

	private <T> T loadBean(WebContext context, Class<T> type) {
		T obj = ReflectionUtils.newInstance(type);
		for (Method m : type.getMethods() ){
			if ( m.getName().startsWith("set")){
				String paramName = m.getName().substring(4);	
				ReflectionUtils.invoke(m.getReturnType(), m, obj, context.getAttribute(paramName, m.getParameterTypes()[0]));
			}
		}
		return obj;
	}


	





}
