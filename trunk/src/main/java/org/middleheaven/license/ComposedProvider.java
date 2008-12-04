package org.middleheaven.license;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ComposedProvider implements LicenceProvider {

	List<LicenceProvider> providers = new CopyOnWriteArrayList<LicenceProvider>();
	
	public void addProvider (LicenceProvider other){
		providers.add(other);
	}
	
	@Override
	public License getLicence(String featureID, String version) {
		for (LicenceProvider p: providers){
			License lic = p.getLicence(featureID, version);
			if (lic.isValid()){
				return lic;
			}
		}
		return new VoidLicence(featureID);
	}


}
