package org.middleheaven.core.reflection.metaclass;

/**
 * An interface for a package object.
 * A {@link MetaPackage} provides an iterator for its MetaClasses.
 * @see MetaClass
 */
public interface MetaPackage extends Iterable<MetaClass>{

	/**
	 * The name of the package.
	 * @return name of the package.
	 */
	public String getName();
	

}
