package org.middleheaven.core.wiring;

public class BindingNotFoundException extends BindingException {


	private static final long serialVersionUID = -7707327481769174794L;

	public BindingNotFoundException(Class<?> contract) {
		super("Binding not found for " + contract.getName());
	}

}
