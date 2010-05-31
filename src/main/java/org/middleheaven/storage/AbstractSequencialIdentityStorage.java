package org.middleheaven.storage;

import org.middleheaven.sequence.Sequence;
import org.middleheaven.util.identity.Identity;

public abstract class AbstractSequencialIdentityStorage extends AbstractDataStorage {

	public AbstractSequencialIdentityStorage(StorableModelReader reader) {
		super(reader);
	}

	/**
	 * Returns a sequence of Identity objects registered for a given name
	 * This method is intended to provide universal support for
	 * storage unique key generation in a store dependent manner.
	 * DatabaseStorages can use native sequence support where available. 
	 * @param name sequence name
	 * @return Sequence of <code>Identity</code> sequence
	 */
	protected abstract <I extends Identity> Sequence<I> getSequence(Class<?> entityType);

	
	@Override
	public Storable assignIdentity(Storable storable) {
		storable.setIdentity(this.getSequence(storable.getPersistableClass()).next().value());
		return storable;
	}

}
