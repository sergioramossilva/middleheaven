package org.middleheaven.web.processing.action;


public interface WebCommandMapping {


	public boolean matches(CharSequence url);
	public Outcome execute(WebContext context);

}
