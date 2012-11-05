package org.middleheaven.core.wiring;

import org.middleheaven.core.annotations.Component;

@Component
public interface BindConfiguration {
	
	public void configure ( Binder binder );
}
