package org.middleheaven.global.address;

import java.text.Format;


public class DefaultAddressModel implements AddressModel {

	private static final DefaultAddressModel DEFAULT = new DefaultAddressModel();
	
	public static DefaultAddressModel getInstance(){
		return DEFAULT;
	}
	
	public DefaultAddressModel(){}
	
	@Override
	public boolean supportsPostalCode() {
		return false;
	}

	@Override
	public Format getPostalCodeFormat() {
		return new PostalCodeFormat("#####-000");
	}


}
