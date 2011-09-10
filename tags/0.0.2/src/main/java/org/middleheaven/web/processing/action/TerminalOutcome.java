package org.middleheaven.web.processing.action;

import org.middleheaven.web.processing.Outcome;

public class TerminalOutcome extends Outcome {

	public TerminalOutcome() {
		super(BasicOutcomeStatus.TERMINATE, "", "text/html");
	}

}
