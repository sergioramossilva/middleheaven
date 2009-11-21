package org.middleheaven.util.identity;

import org.middleheaven.util.coersion.AbstractTypeCoersor;

public class StringIdentityCoersor extends AbstractTypeCoersor<String, Identity> {

	@Override
	public <T extends Identity> T coerceForward(String value, Class<T> type) {
		if (value ==null){
			return null;
		}
		if (type.isAssignableFrom(IntegerIdentity.class)){
			return type.cast(new IntegerIdentity(Integer.parseInt(value)));
		}
		return null;
	}

	@Override
	public <T extends String> T coerceReverse(Identity value, Class<T> type) {
		if (value ==null){
			return null;
		}
		
		return type.cast(value.toString());
	}




}
