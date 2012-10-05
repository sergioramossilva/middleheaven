package org.middleheaven.application;

import java.util.Collection;

import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.util.Version;

/**
 * A registry like service for applications.
 * Applications are identified by a string designated the <code>applicationId<code> and a {@link Version}.
 * An {@link ApplicationActivator} can be registered in order to responde to application lifecycle events.
 * 
 * After regisstring and application, an {@link ApplicationRegistry} is created where other application information can be acessed.
 * 
 * The {@link ApplicationService} is a vital component in the MiddleHeaven platform as all MiddleHeaven solutions have , at least, one application.
 */
@Service
public interface ApplicationService {

	/**
	 * Creates an ApplicationRegistry for the application identified by the <code>applicationId<code>  and <code>version<code>.
	 * 
	 * If the same applicationId was already register an {@link ApplicationAlreadyRegistredException} is throwned.  
	 * @param applicationId any string used as the application mnemonic id
	 * @param version the application version.
	 * @param activator the application activator
	 * @return the application's {@link ApplicationRegistry} object
	 * @throws ApplicationAlreadyRegistredException when the same applicationId was already register.
	 */
	public ApplicationRegistry registry(String applicationId, Version version, ApplicationActivator activator);
	
	/**
	 * Returns the application's {@link ApplicationRegistry} object, or <code>null</code> if this appliation is not registered.
	 * @param applicationId any string used as the application mnemonic id
	 * @return the application's {@link ApplicationRegistry} object, or <code>null</code> if this appliation is not registered.
	 */
	public ApplicationRegistry getRegistry(String applicationId);
	
	/**
	 * 
	 * @return an unmodifiable collection of all {@link ApplicationRegistry}'s
	 */
	public Collection<ApplicationRegistry> getAll();
	
}
