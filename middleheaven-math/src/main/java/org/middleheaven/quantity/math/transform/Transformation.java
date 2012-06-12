package org.middleheaven.quantity.math.transform;

public interface Transformation <T> {

	
	public T fowardTransform(T data);
	public T reverseTransform(T data);
	
}
