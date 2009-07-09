package org.middleheaven.web.processing;

import org.middleheaven.web.processing.action.RequestResponseWebContext;


public interface RenderingProcessor {

	public void process(RequestResponseWebContext context, Outcome outcome) throws HttpProcessException;
}
