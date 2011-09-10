package org.middleheaven.license;



public interface LicenseProvider {

	public License getLicence(String featureID, String version);
	
}
