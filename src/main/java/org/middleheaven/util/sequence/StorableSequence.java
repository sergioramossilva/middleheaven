package org.middleheaven.util.sequence;

import org.middleheaven.util.identity.IntegerIdentity;

public interface StorableSequence<T> extends Sequence<T> {

	
	public void setSeed(IntegerIdentity seed);

	public IntegerIdentity readLast();
	
}
