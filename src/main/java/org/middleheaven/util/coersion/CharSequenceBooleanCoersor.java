package org.middleheaven.util.coersion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CharSequenceBooleanCoersor extends AbstractTypeCoersor<CharSequence, Boolean> {

	@Override
	public <T extends Boolean> T coerceForward(CharSequence cvalue, Class<T> targetClass) {
		
		if (targetClass==null){
			 throw new IllegalArgumentException("Target class is required");
		}
		if (cvalue==null){
			return null;
		}
		
		return (T) Boolean.valueOf(cvalue.toString());
		
	}

	@Override
	public <T extends CharSequence> T coerceReverse(Boolean value, Class<T> targetClass) {
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
