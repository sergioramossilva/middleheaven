package org.middleheaven.core.wiring;

import org.middleheaven.util.measure.GroupMultiplicative2.T;

public class CannotResolveException extends BindingException {

	public CannotResolveException(Class<?> contract) {
		super("Cannot resolve instance of" +  contract.getName());
	}

}
