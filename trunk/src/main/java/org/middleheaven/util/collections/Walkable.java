package org.middleheaven.util.collections;

public interface Walkable <T>{

	public void each(Walker<T> walker);
}
