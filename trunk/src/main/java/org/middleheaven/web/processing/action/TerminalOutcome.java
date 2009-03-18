package org.middleheaven.web.processing.action;

public class TerminalOutcome extends Outcome {

	public TerminalOutcome() {
		super(OutcomeStatus.TERMINATE, false, null);
	}

}
