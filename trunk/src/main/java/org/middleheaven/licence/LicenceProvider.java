package org.middleheaven.licence;



public interface LicenceProvider {

	public Licence getLicence(String featureID, String version);
	
}
