package org.middleheaven.core.wiring;


public class CannotResolveException extends BindingException {

	public CannotResolveException(Class<?> contract,String name) {
		super("Cannot resolve instance of" +  contract.getName() + " named " + name);
	}

}
