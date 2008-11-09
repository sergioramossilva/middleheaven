package org.middleheaven.core.dependency;

import java.util.List;

import org.middleheaven.core.wiring.WiringContext;

public interface Starter <T>{

	public List<T> sort(List<T> dependencies);
	
	public void inicialize (T dependableProperties,WiringContext wiringContext) throws InicializationNotResolvedException,InicializationNotPossibleException;
	
}
