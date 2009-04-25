package org.middleheaven.core.wiring;

public interface ConnectableBinder {

	
	public void addWiringModelParser(WiringModelReader parser);
	public void removeWiringModelParser(WiringModelReader parser);
}
