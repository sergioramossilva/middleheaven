package org.middleheaven.web.rendering;

import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.server.Outcome;
import org.middleheaven.process.web.server.action.RequestResponseWebContext;


public interface RenderingProcessor {

	public void process(RequestResponseWebContext context, Outcome outcome , String contentType) throws HttpProcessException;
}
