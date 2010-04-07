package org.middleheaven.validation;

public class MessageInvalidationReason implements InvalidationReason{

	
	private InvalidationSeverity severity;
	private String message;
	private Object[] params;
	
	public static MessageInvalidationReason invalid(){
		return new MessageInvalidationReason(InvalidationSeverity.ERROR, "invalid");
	}
	
	public static MessageInvalidationReason error(String message){
		return new MessageInvalidationReason(InvalidationSeverity.ERROR, message);
	}
	
	public static MessageInvalidationReason warn(String message){
		return new MessageInvalidationReason(InvalidationSeverity.ERROR, message);
	}
	
	public static MessageInvalidationReason error(String message, Object ... params){
		return new MessageInvalidationReason(InvalidationSeverity.ERROR, message, params);
	}
	
	public static MessageInvalidationReason warn(String message, Object ... params){
		return new MessageInvalidationReason(InvalidationSeverity.ERROR, message, params);
	}
	

	private MessageInvalidationReason(InvalidationSeverity severity, String message, Object ... params) {
		super();
		this.severity = severity;
		this.message = message;
		this.params = params;
	}

	@Override
	public InvalidationSeverity getSeverity() {
		return severity;
	}

	public String getMessage() {
		return message;
	}

	public Object[] getParams() {
		return params;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof MessageInvalidationReason
				&& equals((MessageInvalidationReason) other);
	}

	public boolean equals(MessageInvalidationReason other) {
		return this.severity.equals(other.severity) && this.message.equals(other.message);
	}

	@Override
	public int hashCode() {
		return this.severity.hashCode() ^ this.message.hashCode();
	}
	
	@Override
	public String toString(){
		return this.message;
	}
}
