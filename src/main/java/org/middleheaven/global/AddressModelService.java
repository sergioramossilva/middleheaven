package org.middleheaven.global;

import org.middleheaven.global.address.AddressModel;
import org.middleheaven.global.atlas.Country;

public interface AddressModelService {

	public AddressModel getAddressModel(Country country);

}
