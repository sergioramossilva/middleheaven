package org.middleheaven.injection.mock;

import org.middleheaven.core.wiring.Wire;

public class CyclicDisplayer implements Displayer {

	@Wire Greeter other; 
	
	@Override
	public void display(String s) {
		other.setMessage(new StringMessage(s));
	}

}
