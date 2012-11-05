package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.annotations.Component;

@Component
public class MockDisplay implements Displayer {

	String s;
	@Override
	public void display(String s) {
		this.s = s;	
	}

	public String getSaing(){
		return s;
	}
}
