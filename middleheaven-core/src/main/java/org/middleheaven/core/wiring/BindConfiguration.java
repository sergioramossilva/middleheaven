package org.middleheaven.core.wiring;

import org.middleheaven.core.wiring.annotations.Component;

@Component
public interface BindConfiguration {
	
	public void configure ( Binder binder );
}
