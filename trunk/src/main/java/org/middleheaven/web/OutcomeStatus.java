package org.middleheaven.web;

public class OutcomeStatus{

	public static final OutcomeStatus SUCCESS = new OutcomeStatus("success"); // all ok
	public static final OutcomeStatus FAILURE = new OutcomeStatus("failure"); // exception thrown
	public static final OutcomeStatus ERROR = new OutcomeStatus("error");// error thrown
	public static final OutcomeStatus INVALID = new OutcomeStatus("constrainViolation");// validation failed
	public static final OutcomeStatus TERMINATE = new OutcomeStatus("terminate");// presenter write the response. do not forward

	private String name;
	private OutcomeStatus(String name) {
		this.name = name;
	}

	public boolean equals(Object other) {
		return other instanceof OutcomeStatus && equals((OutcomeStatus) other);
	}

	public boolean equals(OutcomeStatus other) {
		return this.name.equals(other.name);
	}

	public int hashCode() {
		return name.hashCode();
	}
	
	public String toString(){
		return name;
	}



}
