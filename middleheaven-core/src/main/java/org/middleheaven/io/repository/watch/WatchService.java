package org.middleheaven.io.repository.watch;

import org.middleheaven.io.repository.ManagedFile;

/**
 * 
 */
public interface WatchService {


	/**
	 * Register a {@link ManagedFile} to be watched for changes of a given set of kinds, inside a given {@link Watchable}.
	 * 
	 * @param watchable the watchable where the events will occur.
	 * @param managedFile the file to watch
	 * @param events kind of events to watch for.
	 * @return a {@link WatchEventChannel} for receiving the events.
	 */
	public WatchEventChannel register(Watchable watchable, ManagedFile managedFile, WatchEvent.Kind ... events);

	/**
	 * Close the service.
	 * This stops all registered channels and internal monitors.
	 */
	public void close();
	
}
