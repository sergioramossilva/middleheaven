package org.middleheaven.storage;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import org.middleheaven.events.EventListenersSet;
import org.middleheaven.transactions.XAResourceAdapter;
import org.middleheaven.util.criteria.entity.EntityCriteria;
import org.middleheaven.util.identity.Identity;

class SessionAwareEntityStore extends XAResourceAdapter implements EntityStore  {

	private final StorableStateManager manager;
	private final StorageUnit unit = new ArrayStorageUnit();
	
	EventListenersSet<DataStorageListener> listeners = EventListenersSet.newSet(DataStorageListener.class); 
	
	public SessionAwareEntityStore(StorableStateManager manager) {
		this.manager = manager;
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
	public void addStorageListener(DataStorageListener listener) {
		listeners.addListener(listener);
	}

	@Override
	public void removeStorageListener(DataStorageListener listener) {
		listeners.removeListener(listener);
	}

	void fireAddEvent(Object instance) {
		StorageChangeEvent event = new StorageChangeEvent(instance, false,
				true, false);

		listeners.broadcastEvent().onStorageChange(event);
		
		event = null;
	}

	void fireRemovedEvent(Object instance) {
		StorageChangeEvent event = new StorageChangeEvent(instance, true,
				false, false);

		listeners.broadcastEvent().onStorageChange(event);
		
		event = null;
	}

	void fireUpdatedEvent(Object instance) {
		StorageChangeEvent event = new StorageChangeEvent(instance, false,
				false, true);

		listeners.broadcastEvent().onStorageChange(event);
		

		event = null;
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
