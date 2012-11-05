package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.annotations.Component;
import org.middleheaven.core.annotations.Named;
import org.middleheaven.core.annotations.Wire;

@Component
public class HelloMessage extends Message {

	@Wire @Named("hello.message") String text;
	@Wire @Named("hello.name") String name;
	
	public String getText(){
		return text + ", " + name;
	}
}
