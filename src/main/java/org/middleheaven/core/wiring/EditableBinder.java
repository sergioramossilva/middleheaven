package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Set;

interface EditableBinder {

	
	public void addBinding(Binding binding);
	public <T> T getInstance(Class<T> type,  Set<Annotation> specificationsSet);
	public void removeBinding(Binding binding);

}
