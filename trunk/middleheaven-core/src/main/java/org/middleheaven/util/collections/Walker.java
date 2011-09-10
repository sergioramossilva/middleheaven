package org.middleheaven.util.collections;

/**
 * Interface for the visitor pattern used with {@link Walkable} and {@link TreeWalkable}
 */
public interface Walker<T> {

	public void doWith(T object);
}
