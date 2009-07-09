package org.middleheaven.aas.old;

import org.middleheaven.aas.Permission;
import org.middleheaven.aas.Subject;

class DefaultResourceAcessor implements Subject {

	
	private String name;

	public DefaultResourceAcessor(String name){
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hasPermission(Permission permit) {
		// TODO implement DefaultResourceAcessor.hasPermission
		return false;
	}

}
