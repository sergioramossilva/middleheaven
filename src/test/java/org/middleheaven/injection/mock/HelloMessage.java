package org.middleheaven.injection.mock;

import org.middleheaven.core.wiring.Property;
import org.middleheaven.core.wiring.Wire;


public class HelloMessage extends Message {

	@Wire @Property("hello.message") String text;
	@Wire @Property("hello.name") String name;
	
	public String getText(){
		return text + ", " + name;
	}
}
