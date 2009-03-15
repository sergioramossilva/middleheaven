package org.middleheaven.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;


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

	public java.lang.Number increment(Byte b){
		return new Byte((byte)(b.byteValue()+passe.byteValue()));
	}
	
	public java.lang.Number increment(java.lang.Integer b){
		return new java.lang.Integer(b.intValue()+passe.intValue());
	}
	
	public java.lang.Number increment(Short b){
		return new Short((short)(b.shortValue()+passe.shortValue()));
	}
	
	public java.lang.Number increment(Long b){
		return new Long(b.longValue()+passe.longValue());
	}
	
	public java.lang.Number increment(Double b){
		return  new Double(b.doubleValue()+passe.doubleValue());
	}
	
	public java.lang.Number increment(Float b){
		return  new Float(b.floatValue()+passe.floatValue());
	}
	
	public java.lang.Number increment(BigDecimal b){
		return  ((BigDecimal)b).add(new BigDecimal(passe.toString()));
	}
	
	public java.lang.Number increment(BigInteger b){
		return  ((BigInteger)b).add(new BigInteger(passe.toString()));
	}
}
