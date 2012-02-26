package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.HttpRelativeUrl;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;

/**
 * Forward the request to the same URL it come from.
 */
public class ForwardToLast implements OutcomeResolver {

	private WebCommandMappingService mappingService;
	private String lastAction;

	/**
	 * 
	 * Constructor.
	 * @param mappingService
	 * @param lastAction
	 */
	public ForwardToLast (WebCommandMappingService mappingService, String lastAction){
		this.mappingService = mappingService;
		this.lastAction =lastAction;
		
	}
	
	@Override
	public Outcome resolveOutcome(OutcomeStatus status, HttpServerContext context) {
		
		HttpUrl refererUrl = context.getRefererUrl();
		
		HttpRelativeUrl newUrl = new HttpRelativeUrl(refererUrl.getFilename(), refererUrl.getContext());
		
		WebCommandMapping mapping = mappingService.resolve(newUrl);
		
		return mapping.resolveOutcome(lastAction, status, context);
	}

}
