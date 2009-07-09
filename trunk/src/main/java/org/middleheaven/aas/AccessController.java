package org.middleheaven.aas;

import org.middleheaven.core.wiring.service.Service;

@Service
public final class AccessController implements AccessControlService {


	public AccessController(){}


	@Override
	public AccessRequestBroker accessRequestBroker() {
		// TODO implement AccessControlService.accessRequestBroker
		return null;
	}
}
