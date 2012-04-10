package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.wiring.annotations.Component;
import org.middleheaven.core.wiring.annotations.Wire;

@Component
public class A {
	
	private X x;
	
	@Wire private X x2;

	@Wire
	public void setX(X x){
		this.x = x;
	}
	
	public X getX(){
		return x;
	}
	
	/**
	 * @return
	 */
	public X getX2() {
		return x2;
	}
}
