package org.middleheaven.aas;

import org.middleheaven.core.annotations.Service;

@Service
public final class StandardAccessControlService implements AccessControlService {


	AccessRequestBroker broker = new AccessRequestBroker();
	
	public StandardAccessControlService(){};


	@Override
	public AccessRequestBroker accessRequestBroker() {
		return broker;
	}


	@Override
	public void addAuthenticationModule(AuthenticationModule module, SuccessLevel level) {
		broker.addAuthenticationModule(module, level);
	}


	@Override
	public void addAutorizationModule(AutorizationModule module) {
		broker.addAutorizationModule(module);
	}


}
