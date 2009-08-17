package org.middleheaven.storage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.domain.DomainModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.sequence.DefaultToken;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.sequence.SequenceToken;
import org.middleheaven.storage.criteria.Criteria;
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
		return dataStorage.createQuery(criteria, dataStorage
				.storableModelOf(domainModel.getEntityModelFor(criteria
						.getTargetClass())), strategy);
	}

	@Override
	public <T> T store(T obj) {

		Storable p = this.dataStorage.merge(obj);
		Set<Storable> all = new HashSet<Storable>();
		flatten(p,all);
		for (Storable s : all){
			doStore(s);
		}
		
		return (T) p;
	}

	private Collection<Storable> flatten(Storable p, Set<Storable> all){

		if (all.contains(p)){ // loop
			return all;
		}
		
		all.add(p);
		
		EntityModel entityModel = domainModel.getEntityModelFor(p.getPersistableClass());
		
		StorableEntityModel model = this.dataStorage.storableModelOf(entityModel);
		
		for (StorableFieldModel fieldModel : model.fields()){
			if(fieldModel.getDataType().isToOneReference()){
				Storable merged = this.dataStorage.merge(p.getFieldValue(fieldModel));
				if(merged !=null){
					p.setFieldValue(fieldModel, merged);
					flatten(merged, all);
				}
			} else if (fieldModel.getDataType().isToManyReference()){
				Collection<?> allRefereed = (Collection<?>)p.getFieldValue(fieldModel);
				for (Object o : allRefereed){
					if(o !=null){
						Storable merged = this.dataStorage.merge(o);
						p.removeFieldElement(fieldModel, o);
						p.addFieldElement(fieldModel, merged);
						flatten(merged, all);
					}
				}
			}
		}
		return all;
	}
	
	private final void doStore(Storable p) {
		switch (p.getStorableState()) {
		case DELETED:
			doDelete(p);
			break;
		case EDITED:
			doUpdate(p);
			break;
		case FILLED:
			doInsert(p);
			break;
		case BLANK:
		case RETRIVED:
			// not-op
			break;
		default:
			throw new IllegalStateException(p.getStorableState()
					+ " is unkown");
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

	private void doInsert(Storable p) {
		if (p.getIdentity() != null) {
			doUpdate(p);
		}

		// assign key
		this.dataStorage.assignIdentity(p);

		EntityModel entityModel = domainModel.getEntityModelFor(p
				.getPersistableClass());
		this.dataStorage.insert(Collections.singleton(p), dataStorage
				.storableModelOf(entityModel));

		p.setStorableState(StorableState.RETRIVED);
		fireAddEvent(p);
	}

	private void doUpdate(Storable p) {
		if (p.getIdentity() == null) {
			doInsert(p);
		}

		this.dataStorage.update(Collections.singleton(p), dataStorage
				.storableModelOf(domainModel.getEntityModelFor(p
						.getPersistableClass())));

		p.setStorableState(StorableState.RETRIVED);
		fireUpdatedEvent(p);
	}

	private void doDelete(Storable p) {
		if (p.getIdentity() == null) {
			return;
		}

		this.dataStorage.remove(Collections.singleton(p), dataStorage
				.storableModelOf((domainModel.getEntityModelFor(p
						.getPersistableClass()))));

		p.setStorableState(StorableState.DELETED);
		fireRemovedEvent(p);
	}

	@Override
	public <T> void remove(T obj) {
		if (obj instanceof Storable) {
			Storable p = (Storable) obj;
			p.setStorableState(StorableState.DELETED);
			doStore(p);
		}
		// else
		// not managed
		// do nothing as this obj is not in the store

	}

	@Override
	public <T> void remove(Criteria<T> criteria) {
		this.dataStorage.remove(criteria, dataStorage
				.storableModelOf(domainModel.getEntityModelFor(criteria
						.getTargetClass())));
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
