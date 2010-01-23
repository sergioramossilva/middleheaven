package org.middleheaven.aas;

import java.util.Set;

import org.middleheaven.quantity.time.Period;

public class TemporarySignaturePolicy  implements SignaturePolicy{

	
	private Period timeOut;
	public TemporarySignaturePolicy (Period timeOut){
		this.timeOut = timeOut;
	}
	
	@Override
	public Signature createSignature(Set<Credential> credentials) {
		return new TimedSignature(credentials , timeOut);
	}

}
