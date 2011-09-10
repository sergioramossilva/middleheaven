package org.middleheaven.aas;


public interface AccessControlService {


	public void addAuthenticationModule(AuthenticationModule module, SuccessLevel level);
	public void addAutorizationModule(AutorizationModule module);
	
	public AccessRequestBroker accessRequestBroker();
	
}
