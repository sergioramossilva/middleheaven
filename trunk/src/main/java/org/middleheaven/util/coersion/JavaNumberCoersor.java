/**
 * 
 */
package org.middleheaven.util.coersion;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 
 *
 */
public class JavaNumberCoersor implements ObjectCoersor<Number> {

	@Override
	public <T> T convert(Number value, Class<T> targetClass) {

		// if object is of correct type, return it
        if (targetClass.isInstance(value)){
            return targetClass.cast(value);
        }

        if (targetClass.equals(String.class)){
            return targetClass.cast(value.toString());
        } else if (targetClass.equals(Byte.class)){
            return targetClass.cast(new Byte(value.byteValue()));
        } else if (targetClass.equals(Short.class)){
            return targetClass.cast(new Short(value.shortValue()));
        } else if (targetClass.equals(Integer.class)){
            return targetClass.cast(new Integer(value.intValue()));
        } else if (targetClass.equals(Long.class)){
            return targetClass.cast(new Long(value.longValue()));
        } else if (targetClass.equals(BigDecimal.class)){
            return targetClass.cast(new BigDecimal(value.toString()));
        } else if (targetClass.equals(BigInteger.class)){
            return targetClass.cast(new BigInteger(value.toString()));
        } else if (targetClass.equals(Double.class)){
            return targetClass.cast(new Double(value.doubleValue()));
        } else if (targetClass.equals(Float.class)){
            return targetClass.cast(new Float(value.floatValue()));
        } else {
            throw new IllegalArgumentException("Cannot convert " + value.getClass().getName() + " to " + targetClass.getName());
        }
	}
	

    

}
