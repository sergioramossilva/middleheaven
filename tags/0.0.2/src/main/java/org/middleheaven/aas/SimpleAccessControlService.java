package org.middleheaven.aas;

import org.middleheaven.core.wiring.service.Service;

@Service
public final class SimpleAccessControlService implements AccessControlService {


	AccessRequestBroker broker = new AccessRequestBroker();
	
	public SimpleAccessControlService(){};


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
