package org.middleheaven.util.identity;

import org.middleheaven.util.conversion.AbstractTypeConverter;

public class StringIdentityConverter extends AbstractTypeConverter<String, Identity> {

	@Override
	public <T extends Identity> T convertFoward(String value, Class<T> type) {
		if (value ==null){
			return null;
		}
		if (type.isAssignableFrom(IntegerIdentity.class)){
			return type.cast(new IntegerIdentity(Integer.parseInt(value)));
		}
		return null;
	}

	@Override
	public <T extends String> T convertReverse(Identity value, Class<T> type) {
		if (value ==null){
			return null;
		}
		
		return type.cast(value.toString());
	}


}
