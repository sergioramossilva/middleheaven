package org.middleheaven.util.measure.impl;

import org.middleheaven.util.measure.Complex;
import org.middleheaven.util.measure.Integer;
import org.middleheaven.util.measure.Number;
import org.middleheaven.util.measure.NumberFactory;
import org.middleheaven.util.measure.Real;

public class StandardNumberFactory extends NumberFactory {


	@Override
	protected <T extends Number<?>> T numberFor(Class<T> superclass , Object ... values) {
		if (values.length==1){
			if (superclass.equals(Complex.class)){
				return superclass.cast(new BigComplex(values[0].toString()));
			} else if (superclass.equals(Real.class)){
				return superclass.cast(new BigDecimalReal(values[0].toString()));
			} else if (superclass.equals(Integer.class)){
				return superclass.cast(new LongInteger(values[0].toString()));
			} else {
				throw new IllegalArgumentException(superclass.getName() + " is not a recognized Number");
			}
		} else if (values.length==2){
			if (superclass.equals(Complex.class)){
				return superclass.cast(new BigComplex((Real)values[0], (Real)values[1]));
			} 
		} 
		throw new IllegalArgumentException("Array bigger than 1 is not supported for type " + superclass.getName());
	
	}

}
