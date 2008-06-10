package org.middleheaven.injection.mock;

public class StringMessage extends Message {

	private String msg;

	public StringMessage(String msg) {
		super();
		this.msg = msg;
	}
	
	public String getText(){
		return msg;
	}
}
