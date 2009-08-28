package org.middleheaven.storage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.domain.DomainModel;
import org.middleheaven.sequence.DefaultToken;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.sequence.SequenceToken;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Walker;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IdentitySequence;

public class DomainStore implements EntityStore {

	DataStorage dataStorage;
	DomainModel domainModel;
	Set<DataStorageListener> listeners = new CopyOnWriteArraySet<DataStorageListener>();

	public DomainStore(DataStorage dataStorage, DomainModel domainModel) {
		this.dataStorage = dataStorage;
		this.domainModel = domainModel;
	}

	public final Identity getIdentityFor(Object object) {
		if (object instanceof Storable) {
			return ((Storable) object).getIdentity();
		}
		return null;
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria) {
		return this.createQuery(criteria, ReadStrategy.none());
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria, ReadStrategy strategy) {
		return dataStorage.createQuery(criteria , strategy);
	}


	@Override @SuppressWarnings("unchecked") // the merge method guarantees the same type
	public <T> T store(T obj) {

		Storable p = this.dataStorage.merge(obj);
		final Set<Storable> all = new HashSet<Storable>();
		dataStorage.flatten(p,all);


		CollectionUtils.enhance(all).collect(new Classifier<StoreAction, Storable>(){

			@Override
			public StoreAction classify(Storable s) {
				return assignAction(s);
			}

		}).each(new Walker<StoreAction>(){

			@Override
			public void doWith(StoreAction action) {
				action.execute();
			}

		});



		return (T)p;
	}

	private StoreAction assignAction(Storable p){
		switch (p.getStorableState()) {
		case DELETED:
			if (p.getIdentity() == null) {
				return null;
			}
			return new DeleteAction(p);
		case FILLED:
		case EDITED:
			if (p.getIdentity() == null) {
				dataStorage.assignIdentity(p);
				return new InsertAction(p);
			} else {
				// update
				return new UpdateAction(p);
			}
		case BLANK:
		case RETRIVED:
			// not-op
			return null;
		default:
			throw new IllegalStateException(p.getStorableState()
					+ " is unkown");
		}
	}


	private abstract class  StoreAction{


		protected Storable storable;
		public StoreAction (Storable storable){
			this.storable = storable;
		}

		public abstract void execute();
	}
	
	private class InsertAction extends StoreAction{

		public InsertAction (Storable storable){
			super(storable);
		}

		@Override
		public void execute() {
			dataStorage.insert(Collections.singleton(storable));

			storable.setStorableState(StorableState.RETRIVED);
			fireAddEvent(storable);

		}

	}

	private class UpdateAction extends StoreAction{

		public UpdateAction (Storable storable){
			super(storable);
		}

		@Override
		public void execute() {
			dataStorage.update(Collections.singleton(storable));

			storable.setStorableState(StorableState.RETRIVED);
			fireUpdatedEvent(storable);
		}

	}

	private class DeleteAction extends StoreAction{

		public DeleteAction (Storable storable){
			super(storable);
		}

		@Override
		public void execute() {
			dataStorage.remove(Collections.singleton(storable));

			storable.setStorableState(StorableState.DELETED);
			fireRemovedEvent(storable);
		}

	}


	private static class DataBaseIntegerIdentitySequence implements
	IdentitySequence<Identity> {

		Sequence<Identity> baseSequence;

		public DataBaseIntegerIdentitySequence(Sequence<Identity> baseSequence) {
			super();
			this.baseSequence = baseSequence;
		}

		@Override
		public SequenceToken<Identity> next() {
			// TODO define LongIdentity
			return new DefaultToken<Identity>(baseSequence.next().value());
		}

	}

	

	@Override
	public <T> void remove(T obj) {
		if (obj instanceof Storable) {
			final Storable p = (Storable) obj;
			p.setStorableState(StorableState.DELETED);
			assignAction(p).execute();
		}
		// else
		// not managed
		// do nothing as this obj is not in the store

	}

	@Override
	public <T> void remove(final Criteria<T> criteria) {
		dataStorage.remove(criteria);
	}

	@Override
	public void addStorageListener(DataStorageListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeStorageListener(DataStorageListener listener) {
		listeners.remove(listener);
	}

	private void fireAddEvent(Object instance) {
		StorageChangeEvent event = new StorageChangeEvent(instance, false,
				true, false);

		for (DataStorageListener listener : listeners) {
			listener.onStorageChange(event);
		}

		event = null;
	}

	private void fireRemovedEvent(Object instance) {
		StorageChangeEvent event = new StorageChangeEvent(instance, true,
				false, false);

		for (DataStorageListener listener : listeners) {
			listener.onStorageChange(event);
		}

		event = null;
	}

	private void fireUpdatedEvent(Object instance) {
		StorageChangeEvent event = new StorageChangeEvent(instance, false,
				false, true);

		for (DataStorageListener listener : listeners) {
			listener.onStorageChange(event);
		}

		event = null;
	}
}
