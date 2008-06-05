package org.middleheaven.classification;

import java.util.Collection;

public interface Classification<C,T> {

	
	public C[] groups();
	
	public Collection<T> elements(C group);
	
}
