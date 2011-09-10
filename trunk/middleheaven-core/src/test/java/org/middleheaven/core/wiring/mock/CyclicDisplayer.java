package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.wiring.annotations.Wire;

public class CyclicDisplayer implements Displayer {

	@Wire Greeter other; 
	
	@Override
	public void display(String s) {
		other.setMessage(new StringMessage(s));
	}

}
