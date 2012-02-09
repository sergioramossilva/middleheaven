package org.middleheaven.domain.store;


/**
 * Listener for events on the {@link DomainStore}.
 */
public interface DomainStoreListener {

	/**
	 * Event listener called when some changed as occurred. 
	 * @param event the change event.
	 */
	public void onEntityAdded(DomainChangeEvent event);

	/**
	 * @param domainChangeEvent
	 */
	public void onEntityRemoved(DomainChangeEvent domainChangeEvent);

	/**
	 * @param domainChangeEvent
	 */
	public void onEntityChanged(DomainChangeEvent domainChangeEvent);
}
