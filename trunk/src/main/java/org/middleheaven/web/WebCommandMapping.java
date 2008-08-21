package org.middleheaven.web;

import java.util.List;

public interface WebCommandMapping {


	public List<Interceptor> interceptors();

	public boolean matches(CharSequence url);
	public Outcome execute(WebContext context);

}
