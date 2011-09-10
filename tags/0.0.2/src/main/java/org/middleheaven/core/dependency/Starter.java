package org.middleheaven.core.dependency;

import java.util.Collection;
import java.util.List;

public interface Starter <T>{

	public List<T> sort(Collection<T> dependencies);
	
	public void inicialize (T dependableProperties) throws InicializationNotResolvedException,InicializationNotPossibleException;
	public void inicializeWithProxy (T dependableProperties) throws InicializationNotResolvedException,InicializationNotPossibleException;
	
}
