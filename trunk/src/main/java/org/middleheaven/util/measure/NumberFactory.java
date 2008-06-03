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
	
	protected abstract <T extends Number<?>> T numberFor (String value, Class<T> superclass);

	protected abstract Number<?> one ();
	
	protected abstract Number<?> zero ();
	
	
}
