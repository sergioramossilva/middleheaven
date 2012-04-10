package org.middleheaven.process.web.server;

import org.middleheaven.process.web.UrlMapping;
import org.middleheaven.web.rendering.DefaultJspRenderingProcessorResolver;

// created directly on the WebContainerBoostrap
class ServletHttpServerService extends AbstractHttpServerService {

	/**
	 * Constructor.
	 * @param logger
	 */
	public ServletHttpServerService() {
		super();
		
		addRenderingProcessorResolver("jsp",new DefaultJspRenderingProcessorResolver(),UrlMapping.matchAll());
		
	}




}
