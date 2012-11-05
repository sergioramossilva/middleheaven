package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.annotations.Component;
import org.middleheaven.core.annotations.Wire;

@Component
public class CyclicDisplayer implements Displayer {

	@Wire Greeter other; 
	
	@Override
	public void display(String s) {
		other.setMessage(new StringMessage(s));
	}

}
