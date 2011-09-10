package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.server.Outcome;

public class TerminalOutcome extends Outcome {

	public TerminalOutcome() {
		super(BasicOutcomeStatus.TERMINATE, "", "text/html");
	}

}
