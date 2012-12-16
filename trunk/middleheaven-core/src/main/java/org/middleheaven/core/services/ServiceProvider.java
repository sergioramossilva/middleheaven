/**
 * 
 */
package org.middleheaven.core.services;

/**
 * 
 */
public interface ServiceProvider {

	/**
	 * 
	 * @param specification
	 * @return
	 */
	public Service provide(ServiceSpecification specification) throws ServiceNotAvailableException;
}
