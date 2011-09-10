package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.wiring.annotations.Wire;

public class A {

	
	private X x;

	@Wire
	protected void setX(X x){
		this.x = x;
	}
	
	public X getX(){
		return x;
	}
}
