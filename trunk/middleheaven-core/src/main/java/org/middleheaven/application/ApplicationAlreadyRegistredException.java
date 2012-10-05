/**
 * 
 */
package org.middleheaven.application;

/**
 * When an aplication is registered more than once.
 */
public class ApplicationAlreadyRegistredException extends RuntimeException {


	private static final long serialVersionUID = 8978804266486767505L;
	
	private String applicationId;
	
	/**
	 * 
	 * Constructor.
	 * @param applicationId the applicationId that is repeated in the registry.
	 */
	public ApplicationAlreadyRegistredException(String applicationId) {
		super(applicationId + " was already registered");
		this.applicationId = applicationId;
	}

	/**
	 * Obtains {@link String}.
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	
	
}
