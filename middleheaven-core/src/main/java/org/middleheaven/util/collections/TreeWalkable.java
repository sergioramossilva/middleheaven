package org.middleheaven.util.collections;

public interface TreeWalkable<T> extends Walkable<T> {

	
	public void forEachRecursive(Walker<T> walker);
	public void forEachParent(Walker<T> walker);
}
