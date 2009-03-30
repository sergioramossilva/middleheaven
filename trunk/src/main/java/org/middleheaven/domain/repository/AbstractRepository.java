package org.middleheaven.domain.repository;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractRepository<E> implements Repository<E> {

	private Set<RepositoryListener> listeners = new CopyOnWriteArraySet<RepositoryListener> ();

	@Override
	public void addRepositoryListener(RepositoryListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeRepositoryListener(RepositoryListener listener) {
		listeners.remove(listener);
	}

	protected void fireChangeEvent(E instance, boolean isDeleted, boolean isAdded, boolean isUpdated){
		RepositoryChangeEvent<E> event = new RepositoryChangeEvent<E>(
				this,
				instance,
				isDeleted, isAdded, isUpdated
		);
		
		for (RepositoryListener listener : listeners){
			listener.onRepositoryChanged(event);	
		}
		
		event = null;
	}
	
	protected void fireAddedEvent(E instance){
		fireChangeEvent(instance,false,true,false);
	}
	
	protected void fireUpdatedEvent(E instance){
		fireChangeEvent(instance,false, false, true);
	}
	
	protected void fireRemoveEvent(E instance){
		fireChangeEvent(instance,true, false, false);
	}
	
}
