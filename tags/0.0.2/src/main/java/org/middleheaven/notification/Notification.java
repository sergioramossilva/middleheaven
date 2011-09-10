package org.middleheaven.notification;

public class Notification {
	String type;
	long timestamp;
	long sequenceNumber;
	Object userObject;
	
	public Notification(String type) {
		this(type,0);
	}
	
	public Notification(String type, long sequenceNumber) {
		this(type,System.currentTimeMillis(),sequenceNumber);
	}
	
	public Notification(String type, long timestamp, long sequenceNumber) {
		super();
		this.type = type;
		this.timestamp = timestamp;
		this.sequenceNumber = sequenceNumber;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public Object getUserObject() {
		return userObject;
	}
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}
	public String getType() {
		return type;
	}
	

}
