package org.middleheaven.test.wiring.mock;

import org.middleheaven.core.wiring.Name;
import org.middleheaven.core.wiring.Wire;


public class HelloMessage extends Message {

	@Wire @Name("hello.message") String text;
	@Wire @Name("hello.name") String name;
	
	public String getText(){
		return text + ", " + name;
	}
}
