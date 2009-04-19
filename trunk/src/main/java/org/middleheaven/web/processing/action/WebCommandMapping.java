package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;


public interface WebCommandMapping {


	public boolean matches(CharSequence url);
	public Outcome execute(HttpContext context);

}
