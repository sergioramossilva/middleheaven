package org.middleheaven.util.coersion;

public class NumberBooleanCoersor extends AbstractTypeCoersor<Number, Boolean> {

	@Override
	public <T extends Boolean> T coerceForward(Number value, Class<T> type) {
	
		return (T)(value==null ? null : (value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE ));
	}

	@Override
	public <T extends Number> T coerceReverse(Boolean value, Class<T> type) {

		return (T)(value==null ? null : (value.booleanValue() ? new Integer(1) : new Integer(0))); 
	}





}
