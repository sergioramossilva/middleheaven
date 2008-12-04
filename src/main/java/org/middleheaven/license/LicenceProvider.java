package org.middleheaven.license;



public interface LicenceProvider {

	public License getLicence(String featureID, String version);
	
}
