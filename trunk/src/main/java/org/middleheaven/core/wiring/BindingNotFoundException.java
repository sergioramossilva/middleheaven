package org.middleheaven.core.wiring;

public class BindingNotFoundException extends BindingException {

	public BindingNotFoundException(Class<?> contract) {
		super("Binding not found for " + contract.getName());
	}

}
