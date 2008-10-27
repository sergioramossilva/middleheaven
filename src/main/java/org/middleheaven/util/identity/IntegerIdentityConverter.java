package org.middleheaven.util.identity;

import org.middleheaven.util.conversion.AbstractTypeConverter;

public class IntegerIdentityConverter extends AbstractTypeConverter<Integer, Identity> {

	@Override
	public <T extends Identity> T convertFoward(Integer value, Class<T> type) {
		if (value ==null){
			return null;
		}
		if (type.isAssignableFrom(IntegerIdentity.class)){
			return type.cast(new IntegerIdentity(value.intValue()));
		}
		return null;
	}

	@Override
	public <T extends Integer> T convertReverse(Identity value, Class<T> type) {
		if (value ==null){
			return null;
		}
		if (value.getClass().isAssignableFrom(IntegerIdentity.class)){
			return type.cast(Integer.valueOf(value.toString()));
		}
		return null;
	}



}
