package org.middleheaven.util.identity;

import org.middleheaven.util.conversion.AbstractTypeConverter;

public class IdentityConverter extends AbstractTypeConverter<Identity,CharSequence>{

	@Override
	public <T extends CharSequence> T convertFoward(Identity value, Class<T> targetClass) {
		if (targetClass.equals(String.class) || targetClass.equals(CharSequence.class)){
			return targetClass.cast(value.toString());
		} else if (targetClass.equals(StringBuilder.class)){
			return targetClass.cast(new StringBuilder(value.toString()));
		}else if (targetClass.equals(StringBuffer.class)){
			return targetClass.cast(new StringBuffer(value.toString()));
		} else {
			throw new IllegalArgumentException("Cannot convert " + value.getClass().getName() + " to " + targetClass.getName());
		}
	}

	@Override
	public <T extends Identity> T convertReverse(CharSequence value, Class<T> type) {
		try{
			int id = Integer.parseInt(value.toString());
			return type.cast(new IntegerIdentity(id));
		} catch (NumberFormatException e){
			return type.cast(new UUIDIdentity(value.toString()));
		}
	}


}
