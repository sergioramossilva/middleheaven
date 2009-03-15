package org.middleheaven.util.conversion;

public class NumberBooleanConverter extends AbstractTypeConverter<Number, Boolean> {

	@Override
	public <T extends Boolean> T convertFoward(Number value, Class<T> type) {
	
		return (T)(value==null ? null : (value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE ));
	}

	@Override
	public <T extends Number> T convertReverse(Boolean value, Class<T> type) {

		return (T)(value==null ? null : (value.booleanValue() ? new Integer(1) : new Integer(0))); 
	}



}
