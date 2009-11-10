package org.middleheaven.aas;

import java.util.Set;

public class PermanentSignature implements Signature {

	private Set<Credential>  credentials;
	
	public PermanentSignature(Set<Credential>  credentials){
		this.credentials  = credentials;
	}
	
	@Override
	public Set<Credential> getCredentials() {
		return credentials;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void refresh() {
		//no-op
	}

}
