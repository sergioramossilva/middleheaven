package org.middleheaven.util.identity;

import org.middleheaven.util.coersion.AbstractTypeCoersor;

public class NumberIdentityCoersor<N extends Number> extends AbstractTypeCoersor<N, Identity> {

	private Class<? extends Number> numberClass;

	public static <T extends Number> NumberIdentityCoersor<T> newInstance(Class<T> numberClass){
		return new NumberIdentityCoersor<T>(numberClass);
	}
	
	private NumberIdentityCoersor(Class<? extends Number> numberClass) {
		this.numberClass = numberClass;
	}

	@Override
	public <T extends Identity> T coerceForward(N value, Class<T> type) {
		if (value ==null){
			return null;
		}
		if (type.isAssignableFrom(IntegerIdentity.class)){
			return type.cast(new IntegerIdentity(value.intValue()));
		} else if (type.isAssignableFrom(LongIdentity.class)){
			return type.cast(new LongIdentity(value.longValue()));
		}
		return null;
	}

	@Override
	public <T extends N> T coerceReverse(Identity value, Class<T> type) {
		if (value == null){
			return null;
		}
		if (value.getClass().isAssignableFrom(IntegerIdentity.class)){
			return type.cast(Integer.valueOf(value.toString()));
		} else if (value.getClass().isAssignableFrom(LongIdentity.class)){
			return type.cast(Long.valueOf(value.toString()));
		}
		return null;
	}



}
