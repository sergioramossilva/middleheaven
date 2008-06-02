package org.middleheaven.global.address;

import org.middleheaven.global.atlas.Country;

public interface AddressLocationService {

	public Address locateFromPostalCode(Country country, PostalCode postalCode);
	
}
