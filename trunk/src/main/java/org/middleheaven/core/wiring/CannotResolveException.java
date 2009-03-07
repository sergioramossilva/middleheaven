package org.middleheaven.core.wiring;


public class CannotResolveException extends BindingException {

	public CannotResolveException(Class<?> contract) {
		super("Cannot resolve instance of" +  contract.getName());
	}

}
