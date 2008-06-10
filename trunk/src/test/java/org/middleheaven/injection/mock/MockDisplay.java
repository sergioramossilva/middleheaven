package org.middleheaven.injection.mock;

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
