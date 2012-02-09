package org.middleheaven.domain.store;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.query.Query;
import org.middleheaven.events.EventListenersSet;
import org.middleheaven.transactions.XAResourceAdapter;
import org.middleheaven.util.identity.Identity;

class SessionAwareDomainStore extends XAResourceAdapter implements DomainStore  {

	private final DomainStoreManager manager;
	private final StorageUnit unit = new ArrayStorageUnit();
	
	private final EventListenersSet<DomainStoreListener> listeners = EventListenersSet.newSet(DomainStoreListener.class); 
	
	public SessionAwareDomainStore(IdentityManager identityManager,  EntityInstanceStorage storage, DomainModel domainModel) {
		this.manager = new EntityInstanceStoreManager(identityManager, storage, domainModel);
	}

	public Identity getIdentityFor(Object object) {
		return manager.getIdentityFor(object); 
	}

	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria) {
		return this.createQuery(criteria, ReadStrategy.fowardReadOnly());
	}

	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria, ReadStrategy strategy) {
		return manager.createQuery(criteria , strategy, unit);
	}


	@Override @SuppressWarnings("unchecked") // the merge method guarantees the same type
	public <T> T store(T obj) {
		return manager.store(obj, unit);
	}

	@Override
	public <T> void remove(T obj) {
		manager.remove(obj, unit);
	}

	@Override
	public <T> void remove(final EntityCriteria<T> criteria) {
		
		for (T t : manager.createQuery(criteria, ReadStrategy.fowardReadOnly(), unit).fetchAll()){
			manager.remove(t, unit);
		}
	}

	// listeners
	
	@Override
	public void addStorageListener(DomainStoreListener listener) {
		listeners.addListener(listener);
	}

	@Override
	public void removeStorageListener(DomainStoreListener listener) {
		listeners.removeListener(listener);
	}

	void fireAddEvent(Object instance) {

		listeners.broadcastEvent().onEntityAdded(new DomainChangeEvent(instance));

	}

	void fireRemovedEvent(Object instance) {
		
		listeners.broadcastEvent().onEntityRemoved(new DomainChangeEvent(instance));
		
	}

	void fireUpdatedEvent(Object instance) {
		listeners.broadcastEvent().onEntityChanged(new DomainChangeEvent(instance));
		
	}

    // XAResource 
	
    public synchronized int prepare(Xid xid) throws XAException {
        
    	unit.simplify();
    	
    	return super.prepare(xid);
    }


	@Override
	public synchronized void commit(Xid xid, boolean flag) throws XAException {
		manager.commit(unit);
	}


	@Override
	public synchronized void rollback(Xid xid) throws XAException {
		manager.roolback(unit);
	}



}
