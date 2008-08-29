package org.middleheaven.util.measure;

import org.middleheaven.util.measure.impl.StandardNumberFactory;

public abstract class NumberFactory {

	
	private static NumberFactory currentFactory = new StandardNumberFactory();
	public static NumberFactory getFactory(){
		return currentFactory;
	}
	
	public static void setFactory(NumberFactory factory){
		currentFactory = factory;
	}
	
	protected NumberFactory(){};
	
	protected abstract <T extends Number<?>> T numberFor (Class<T> superclass,Object ... value);


	public <T extends Number<T>>T promote(Number<?> other, Class<T> targetType) {
		Class<?> originType = other.getClass();
		if (originType.equals(targetType)){
			return targetType.cast(other);
		} else {
			return numberFor(targetType, originType.toString());
		}
	}

}
