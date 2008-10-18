package org.middleheaven.util.identity;

import org.middleheaven.util.sequence.DefaultToken;
import org.middleheaven.util.sequence.Sequence;
import org.middleheaven.util.sequence.SequenceState;
import org.middleheaven.util.sequence.SequenceToken;
import org.middleheaven.util.sequence.StateEditableSequence;

public final class IntegerIdentitySequence implements IdentitySequence<IntegerIdentity> , StateEditableSequence<IntegerIdentity> {

	private IntegerIdentity current = new IntegerIdentity(0);

	public IntegerIdentitySequence(){}
	

	@Override
	public SequenceToken<IntegerIdentity> next() {
		current = current.next();
		return new DefaultToken<IntegerIdentity>(current);
	}


	@Override
	public SequenceState getSequenceState() {
		return new SequenceState(current);
	}


	@Override
	public void setSequenceState(SequenceState state) {
		current = (IntegerIdentity)state.getLastUsedValue();
	}

}
