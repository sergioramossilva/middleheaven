package org.middleheaven.license;


public class VoidLicenseProvider implements LicenseProvider {

	
	@Override
	public License getLicence(String featureID, String version) {
		return new VoidLicense(featureID);
	}


}
