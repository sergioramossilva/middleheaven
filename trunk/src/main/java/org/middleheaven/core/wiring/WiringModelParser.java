package org.middleheaven.core.wiring;

public interface WiringModelParser {

	
	public <T> void parse(Class<T> type, WiringModel model);
}
