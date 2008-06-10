package org.middleheaven.licence;

public abstract class AbstractLicence implements Licence {

	private final String feature;
	public AbstractLicence(String feature){
		this.feature= feature;
	}
	
	
	@Override
	public final String getFeatureID() {
		return feature;
	}



}
