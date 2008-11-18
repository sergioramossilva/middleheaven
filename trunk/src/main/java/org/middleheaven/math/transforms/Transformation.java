package org.middleheaven.math.transforms;

public interface Transformation <T> {

	
	public T fowardTransform(T data);
	public T reverseTransform(T data);
	
}
