package org.middleheaven.aas;

import java.util.Set;

public class TemporarySignaturePolicy  implements SignaturePolicy{

	
	private int timeOut;
	public TemporarySignaturePolicy (int timeOut){
		this.timeOut = timeOut;
	}
	
	@Override
	public Signature createSignature(Set<Credential> credentials) {
		return new TimedSignature(credentials , timeOut);
	}

}
