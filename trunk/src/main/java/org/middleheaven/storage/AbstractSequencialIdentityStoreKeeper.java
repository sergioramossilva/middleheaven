package org.middleheaven.storage;

import org.middleheaven.sequence.Sequence;
import org.middleheaven.util.identity.Identity;

public abstract class AbstractSequencialIdentityStoreKeeper extends AbstractStoreKeeper {

	public AbstractSequencialIdentityStoreKeeper(StorableModelReader reader) {
		super(reader);
	}

	/**
	 * Returns a sequence of longs under registered for a given name
	 * This method is intended to provide universal support for
	 * storage unique key generation in a store dependent manner.
	 * DatabaseStorages can use native sequence support where available. 
	 * @param name sequence name
	 * @return Sequence of <code>Identity</code> sequence
	 */
	protected abstract <I extends Identity> Sequence<I> getSequence(String name);

	
	@Override
	public Storable assignIdentity(Storable storable) {
		storable.setIdentity(this.getSequence(storable.getPersistableClass().getName()).next().value());
		return storable;
	}

}
