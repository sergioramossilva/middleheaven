package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.HttpRelativeUrl;


public interface WebCommandMappingService {

	public WebCommandMapping resolve(HttpRelativeUrl url);
	
	


}
