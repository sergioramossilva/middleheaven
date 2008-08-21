package org.middleheaven.web;

import java.util.ArrayList;
import java.util.List;

public class ServiceWebCommandMapping implements WebCommandMapping{

	private WebCommandMapping original;
	private List<Interceptor> genericInterceptors;
	
	public ServiceWebCommandMapping(List<Interceptor> genericInterceptors , WebCommandMapping original){
		this.original = original;
		this.genericInterceptors = genericInterceptors;
	}
	
	@Override
	public Outcome execute(WebContext context) {
		return original.execute(context);
	}

	@Override
	public List<Interceptor> interceptors() {
		List<Interceptor> all = new ArrayList<Interceptor>(genericInterceptors);
		all.addAll(original.interceptors());
		return all;
	}

	@Override
	public boolean matches(CharSequence url) {
		return original.matches(url);
	}
	
	

}
