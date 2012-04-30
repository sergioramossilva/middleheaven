package org.middleheaven.util.validation;


public class PropertyInvalidationReason implements InvalidationReason {

	String message;
	Object[] params;
	InvalidationSeverity severity;
	
	public PropertyInvalidationReason(String name, InvalidationReason reason) {

		message = reason.getMessage();
		
		params = new Object[reason.getParams().length + 1];
		params[0] = reason.getParams()[0];
		params[1] = name;
		System.arraycopy(reason.getParams(), 1, params, 2, reason.getParams().length-1);

		severity = reason.getSeverity();
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public Object[] getParams() {
		return params;
	}

	@Override
	public InvalidationSeverity getSeverity() {
		return severity;
	}

}
