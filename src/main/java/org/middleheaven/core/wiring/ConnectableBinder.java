package org.middleheaven.core.wiring;

public interface ConnectableBinder {

	
	public void addWiringModelParser(WiringModelParser parser);
	public void removeWiringModelParser(WiringModelParser parser);
}
