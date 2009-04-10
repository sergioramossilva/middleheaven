package org.middleheaven.core.wiring;

public interface AfterWiringPoint {

	
	public <T> T writeAtPoint(EditableBinder binder, T object);
	
}
