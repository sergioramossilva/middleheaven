package org.middleheaven.process.web.server;

import org.middleheaven.process.web.UrlMapping;
import org.middleheaven.web.rendering.DefaultJspRenderingProcessorResolver;

// created directly on the WebContainerBoostrap
class ServletHttpServerService extends AbstractHttpServerService {

	public ServletHttpServerService(){

		addRenderingProcessorResolver("jsp",new DefaultJspRenderingProcessorResolver(),UrlMapping.matchAll());
	}


}
