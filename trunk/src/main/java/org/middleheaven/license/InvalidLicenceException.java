package org.middleheaven.license;

public class InvalidLicenceException extends LicenseException {


	private static final long serialVersionUID = 1L;

	public InvalidLicenceException() {
		super("Invalid Licence");
	}

}
