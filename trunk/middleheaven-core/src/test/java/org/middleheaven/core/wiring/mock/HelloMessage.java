package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.wiring.annotations.Named;
import org.middleheaven.core.wiring.annotations.Wire;


public class HelloMessage extends Message {

	@Wire @Named("hello.message") String text;
	@Wire @Named("hello.name") String name;
	
	public String getText(){
		return text + ", " + name;
	}
}
