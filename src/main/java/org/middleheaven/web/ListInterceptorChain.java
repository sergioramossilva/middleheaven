package org.middleheaven.web;

import java.util.ArrayList;
import java.util.List;

public class ListInterceptorChain implements InterceptorChain{

	private List<Interceptor> interceptors;
	private int current=0;
	private ChainListener listener;

	public ListInterceptorChain(List<Interceptor> interceptors,  ChainListener listener){
		this.interceptors = new ArrayList<Interceptor>(interceptors);
		this.listener = listener;
	}
	
	@Override
	public void doChain(WebContext context) {
		if (current<interceptors.size()){
			current++;
			interceptors.get(current-1).intercept(context, this);
		} else {
			listener.doFinal(context);
		}
	}
	

}
