package org.middleheaven.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * {@link Incrementor} implementation for any {@link java.lang.Number}.
 */
public class NumberIncrementor implements Incrementor<java.lang.Number> {

	java.lang.Number passe;
	public NumberIncrementor(java.lang.Number passe){
		this.passe = passe;
	}
	
	@Override
	public java.lang.Number increment(java.lang.Number object) {
		try {
			Method m = this.getClass().getMethod("increment", object.getClass());
			return (java.lang.Number)m.invoke(this, object);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot increment " + object.getClass(),e);
		} 
	}

	/**
	 * Inrement a byte value
	 * @param b the byte value to increment
	 * @return the next byte in ascending order
	 */
	protected java.lang.Number increment(Byte b){
		return Byte.valueOf((byte)(b.byteValue()+passe.byteValue()));
	}
	
	/**
	 * Inrement an integer value
	 * @param b the integer value to increment
	 * @return the next integer in ascending order
	 */
	protected java.lang.Number increment(java.lang.Integer b){
		return  java.lang.Integer.valueOf(b.intValue()+passe.intValue());
	}
	
	/**
	 * Inrement a short value
	 * @param b the short value to increment
	 * @return the next short in ascending order
	 */
	protected java.lang.Number increment(Short b){
		return Short.valueOf((short)(b.shortValue()+passe.shortValue()));
	}
	
	/**
	 * Inrement a long value
	 * @param b the long value to increment
	 * @return the next long in ascending order
	 */
	protected java.lang.Number increment(Long b){
		return Long.valueOf(b.longValue()+passe.longValue());
	}
	
	/**
	 * Inrement a double value
	 * @param b the double value to increment
	 * @return the next double in ascending order
	 */
	protected java.lang.Number increment(Double b){
		return  Double.valueOf(b.doubleValue()+passe.doubleValue());
	}
	
	/**
	 * Inrement a float value
	 * @param b the float value to increment
	 * @return the next float in ascending order
	 */
	protected java.lang.Number increment(Float b){
		return  Float.valueOf(b.floatValue()+passe.floatValue());
	}
	
	/**
	 * Inrement a {@link BigDecimal} value
	 * @param b the {@link BigDecimal} value to increment
	 * @return the next {@link BigDecimal} in ascending order
	 */
	protected java.lang.Number increment(BigDecimal b){
		return  ((BigDecimal)b).add(new BigDecimal(passe.toString()));
	}
	
	/**
	 * Inrement a {@link BigInteger} value
	 * @param b the {@link BigInteger} value to increment
	 * @return the next {@link BigInteger} in ascending order
	 */
	protected java.lang.Number increment(BigInteger b){
		return  ((BigInteger)b).add(new BigInteger(passe.toString()));
	}
}
