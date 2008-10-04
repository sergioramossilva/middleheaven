package org.middleheaven.util.conversion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CharSequenceBooleanConverter extends AbstractTypeConverter<CharSequence, Boolean> {

	@Override
	public <T extends Boolean> T convertFoward(CharSequence cvalue, Class<T> targetClass) {
		
		if (targetClass==null){
			 throw new IllegalArgumentException("Target class is required");
		}
		if (cvalue==null){
			return null;
		}
		
		return (T) Boolean.valueOf(cvalue.toString());
		
	}

	@Override
	public <T extends CharSequence> T convertReverse(Boolean value, Class<T> targetClass) {
		if (targetClass.equals(String.class)){
			return targetClass.cast(value.toString());
		} else if (targetClass.equals(StringBuilder.class)){
			return targetClass.cast(new StringBuilder(value.toString()));
		}else if (targetClass.equals(StringBuffer.class)){
			return targetClass.cast(new StringBuffer(value.toString()));
		} else {
			throw new IllegalArgumentException("Cannot convert " + value.getClass().getName() + " to " + targetClass.getName());
		}
	}





}
