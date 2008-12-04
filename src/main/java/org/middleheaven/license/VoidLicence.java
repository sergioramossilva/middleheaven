/**
 * 
 */
package org.middleheaven.license;


/**
 * An "void" license. This implementation is invalid and throws an 
 * <code>InvalidLicenceException</code> on chekout.
 */
public class VoidLicence implements License{
	
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
		throw new InvalidLicenceException();
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