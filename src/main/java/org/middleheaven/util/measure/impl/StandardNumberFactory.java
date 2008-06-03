package org.middleheaven.util.measure.impl;

import java.math.BigDecimal;

import org.middleheaven.util.measure.Integer;
import org.middleheaven.util.measure.Number;
import org.middleheaven.util.measure.NumberFactory;
import org.middleheaven.util.measure.Real;

public class StandardNumberFactory extends NumberFactory {


	static final BigDecimalReal ONE = new BigDecimalReal(BigDecimal.ONE);
	static final BigDecimalReal ZERO = new BigDecimalReal(BigDecimal.ZERO);

	@Override
	protected Number<?> one() {
		return ONE;
	}

	@Override
	protected Number<?> zero() {
		return ZERO;
	}

	@Override
	protected <T extends Number<?>> T numberFor(String value,Class<T> superclass) {
		if (superclass.equals(Real.class)){
			return (T)new BigDecimalReal(value);
		} else if (superclass.equals(Integer.class)){
			return (T)new LongInteger(value);
		} else {
			throw new IllegalArgumentException(superclass.getName() + " is not a recognized Number");
		}
	}

}
