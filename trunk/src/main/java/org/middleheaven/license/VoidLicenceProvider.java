package org.middleheaven.license;


public class VoidLicenceProvider implements LicenceProvider {

	
	@Override
	public License getLicence(String featureID, String version) {
		return new VoidLicence(featureID);
	}


}
