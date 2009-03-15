package org.middleheaven.util.conversion;


public class CharSequenceConverter extends AbstractTypeConverter<CharSequence, CharSequence> {

	@Override
	public <T extends CharSequence> T convertFoward(CharSequence cvalue, Class<T> targetClass) {
		
		if (targetClass==null){
			 throw new IllegalArgumentException("Target class is required");
		}
		if (cvalue==null){
			return null;
		}
		
		if (targetClass.isInstance(cvalue)){
			return targetClass.cast(cvalue);
		}
		
		if (targetClass.equals(String.class)){
			return targetClass.cast(cvalue.toString());
		} else if (targetClass.equals(StringBuilder.class)){
			return targetClass.cast(new StringBuilder(cvalue.toString()));
		} if (targetClass.equals(StringBuffer.class)){
			return targetClass.cast(new StringBuffer(cvalue.toString()));
		} else {
            throw new IllegalArgumentException("Cannot convert " + cvalue.getClass().getName() + " to " + targetClass.getName());
        }
	}

	@Override
	public <T extends CharSequence> T convertReverse(CharSequence value, Class<T> targetClass) {
		return convertFoward(value,targetClass);
	}





}
