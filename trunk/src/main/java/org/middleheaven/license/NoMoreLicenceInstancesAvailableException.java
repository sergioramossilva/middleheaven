package org.middleheaven.license;

public class NoMoreLicenceInstancesAvailableException extends LicenseException {

	
	public NoMoreLicenceInstancesAvailableException() {
		super("No more licence instances");
	}

	private static final long serialVersionUID = 1L;

}
