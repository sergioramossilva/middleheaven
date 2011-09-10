package org.middleheaven.global.gov.br;

import java.text.Format;

import org.middleheaven.global.address.AddressModel;
import org.middleheaven.global.address.PostalCodeFormat;


public class BRAddressModel implements AddressModel{

	@Override
	public boolean supportsPostalCode() {
		return true;
	}

	@Override
	public Format getPostalCodeFormat() {
		return new PostalCodeFormat("#####-000");
	}

}
