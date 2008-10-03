package org.middleheaven.web;


public interface WebCommandMapping {


	public boolean matches(CharSequence url);
	public Outcome execute(WebContext context);

}
