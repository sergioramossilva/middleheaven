package org.middleheaven.test.wiring.mock;

import org.middleheaven.core.wiring.Wire;

public class Greeter {

    Message message;
	final Displayer display;
	
	@Wire 
	public Greeter(Displayer display){
		this.display = display;
	}
	
	@Wire
	public void setMessage( Message message){
		this.message = message;
	}
	
	public void sayHello(){
		display.display(message.getText());
	}
}
