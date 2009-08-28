package org.middleheaven.util.identity;

import org.middleheaven.util.conversion.AbstractTypeConverter;

public class NumberIdentityConverter<N extends Number> extends AbstractTypeConverter<N, Identity> {

	private Class<? extends Number> numberClass;

	public static <T extends Number> NumberIdentityConverter<T> newInstance(Class<T> numberClass){
		return new NumberIdentityConverter<T>(numberClass);
	}
	
	private NumberIdentityConverter(Class<? extends Number> numberClass) {
		this.numberClass = numberClass;
	}

	@Override
	public <T extends Identity> T convertFoward(N value, Class<T> type) {
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
	public <T extends N> T convertReverse(Identity value, Class<T> type) {
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
