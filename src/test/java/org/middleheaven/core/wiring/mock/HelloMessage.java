package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.wiring.annotations.Name;
import org.middleheaven.core.wiring.annotations.Wire;


public class HelloMessage extends Message {

	@Wire @Name("hello.message") String text;
	@Wire @Name("hello.name") String name;
	
	public String getText(){
		return text + ", " + name;
	}
}
