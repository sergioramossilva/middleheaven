package org.middleheaven.util.identity;

import org.middleheaven.util.sequence.DefaultToken;
import org.middleheaven.util.sequence.SequenceToken;

public class UUIDIdentitySequence implements IdentitySequence<UUIDIdentity>{

	public UUIDIdentitySequence(){}
	
	@Override
	public SequenceToken<UUIDIdentity> next() {
		return new DefaultToken<UUIDIdentity>(UUIDIdentity.next());
	}

}
