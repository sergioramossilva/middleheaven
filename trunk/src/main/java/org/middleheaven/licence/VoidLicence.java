/**
 * 
 */
package org.middleheaven.licence;

public class VoidLicence implements Licence{
	
	private String feature;
	
	public VoidLicence(String featureID){
		this.feature = featureID;
	}
	
	@Override
	public void checkIn() {
		//no-op
	}

	@Override
	public void checkOut() {
		throw new LicenceException("Licence is void");
	}

	@Override
	public String getAttribute(String name) {
		return null;
	}

	@Override
	public String getFeatureID() {
		return feature;
	}

	@Override
	public boolean isValid() {
		return false;
	}
	
}