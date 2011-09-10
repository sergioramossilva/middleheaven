package org.middleheaven.web.rendering;

import org.middleheaven.web.processing.HttpProcessException;
import org.middleheaven.web.processing.Outcome;
import org.middleheaven.web.processing.action.RequestResponseWebContext;


public interface RenderingProcessor {

	public void process(RequestResponseWebContext context, Outcome outcome , String contentType) throws HttpProcessException;
}
