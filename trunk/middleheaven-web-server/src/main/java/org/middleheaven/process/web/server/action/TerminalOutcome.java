package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.server.Outcome;

public class TerminalOutcome extends Outcome {

	public TerminalOutcome() {
		this("text/html");
	}
	
	public TerminalOutcome(String contentType) {
		super(BasicOutcomeStatus.TERMINATE, "", contentType);
	}

}
