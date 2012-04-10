package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.wiring.annotations.Component;

@Component
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
