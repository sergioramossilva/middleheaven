package org.middleheaven.core.dependency;

import org.middleheaven.core.wiring.WiringContext;

public interface Starter <T, D extends DependableProperties>{

	public D wrap(T object);
	
	public void inicialize (D dependableProperties,WiringContext wiringContext) throws InicializationNotResolvedException,InicializationNotPossibleException;
	
}
