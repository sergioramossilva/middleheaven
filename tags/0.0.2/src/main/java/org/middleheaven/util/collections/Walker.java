package org.middleheaven.util.collections;

/**
 * Interface for the visitor pattern used with {@code Walkable} and {@code TreeWalkable}
 */
public interface Walker<T> {

	public void doWith(T object);
}
