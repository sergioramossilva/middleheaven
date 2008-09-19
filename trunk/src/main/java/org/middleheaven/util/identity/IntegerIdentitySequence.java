package org.middleheaven.util.identity;

import org.middleheaven.util.sequence.DefaultToken;
import org.middleheaven.util.sequence.SequenceToken;

public final class IntegerIdentitySequence implements IdentitySequence<IntegerIdentity> , StorableSequence<IntegerIdentity> {

	private IntegerIdentity current = new IntegerIdentity(0);
	
	
	public IntegerIdentitySequence(){}
	

	@Override
	public void setSeed(IntegerIdentity seed) {
		current = seed;
	}

	@Override
	public IntegerIdentity readLast() {
		return current;
	}

	@Override
	public SequenceToken<IntegerIdentity> next() {
		current = current.next();
		return new DefaultToken<IntegerIdentity>(current);
	}

}
