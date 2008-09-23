package org.middleheaven.util.measure;


public interface GroupMultiplicative2<T> {

	public interface T<X>{}; 
	
	public <X> T<X> times(T<?> other);
	
}
