package org.middleheaven.quantity.math.impl;

import org.middleheaven.quantity.math.Complex;
import org.middleheaven.quantity.math.Integer;
import org.middleheaven.quantity.math.Number;
import org.middleheaven.quantity.math.NumberFactory;
import org.middleheaven.quantity.math.Real;

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
