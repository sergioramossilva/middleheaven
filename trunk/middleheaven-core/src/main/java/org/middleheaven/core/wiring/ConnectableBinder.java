package org.middleheaven.core.wiring;

public interface ConnectableBinder  {

	
	public void addWiringModelParser(WiringModelReader parser);
	public void removeWiringModelParser(WiringModelReader parser);
	

	public void addInterceptor(WiringInterceptor interceptor);


	public void removeInterceptor(WiringInterceptor interceptor);
	
}
