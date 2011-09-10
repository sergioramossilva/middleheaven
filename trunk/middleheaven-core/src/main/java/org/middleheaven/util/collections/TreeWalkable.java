package org.middleheaven.util.collections;

public interface TreeWalkable<T> extends Walkable<T> {

	
	public void eachRecursive(Walker<T> walker);
	public void eachParent(Walker<T> walker);
}
