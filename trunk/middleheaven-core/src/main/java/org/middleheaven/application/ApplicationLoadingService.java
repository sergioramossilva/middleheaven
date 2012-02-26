package org.middleheaven.application;

import org.middleheaven.core.wiring.service.Service;

@Service
public interface ApplicationLoadingService {

	
	public ApplicationLoadingCycle getApplicationLoadingCycle();

	public ApplicationContext getApplicationContext();

}
