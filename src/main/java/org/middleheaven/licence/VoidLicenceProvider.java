package org.middleheaven.licence;


public class VoidLicenceProvider implements LicenceProvider {

	
	@Override
	public Licence getLicence(String featureID, String version) {
		return new VoidLicence(featureID);
	}


}
