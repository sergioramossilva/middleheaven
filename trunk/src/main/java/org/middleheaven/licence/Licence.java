package org.middleheaven.licence;

public interface Licence {

	public String getFeatureID();
	public boolean isValid();
	public void checkOut();
	public void checkIn();
	public String getAttribute(String name);
	
}
