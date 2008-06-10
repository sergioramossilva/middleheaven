package org.middleheaven.licence;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ComposedProvider implements LicenceProvider {

	List<LicenceProvider> providers = new CopyOnWriteArrayList<LicenceProvider>();
	
	public void addProvider (LicenceProvider other){
		providers.add(other);
	}
	
	@Override
	public Licence getLicence(String featureID, String version) {
		for (LicenceProvider p: providers){
			Licence lic = p.getLicence(featureID, version);
			if (lic.isValid()){
				return lic;
			}
		}
		return new VoidLicence(featureID);
	}


}
