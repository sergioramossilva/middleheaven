package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;

public class ForwardToLast implements OutcomeResolver {

	
	private WebCommandMappingService mappingService;
	private String lastAction;

	public ForwardToLast (WebCommandMappingService mappingService, String lastAction){
		this.mappingService = mappingService;
		this.lastAction =lastAction;
		
	}
	
	@Override
	public Outcome resolveOutcome(OutcomeStatus status, HttpContext context) {
		
		String newUrl = context.getRefererUrl().getContexlessPath() + context.getRefererUrl().getFilename();
		
		WebCommandMapping mapping = mappingService.resolve(newUrl);
		
		return mapping.resolveOutcome(lastAction, status, context);
	}

}
