package org.middleheaven.aas;

import org.middleheaven.core.wiring.service.Service;

@Service
public final class WebAccessController implements AccessControlService {


	public WebAccessController(){}


	@Override
	public AccessRequestBroker accessRequestBroker() {
		return new AccessRequestBroker();
	}
}
