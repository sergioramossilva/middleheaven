package org.middleheaven.quantity.structure;


public interface GroupAdditive<A> {

	/**
	 * Returns the sum of this object with the one specified.
	 * @param other the object to be added
	 * @return this + other
	 */
	public A plus(A other);
	
	/**
	 * Returns the additive inverse of this object.
	 * @return -this
	 */
	public A negate();
	
	/**
	 * Returns the sum of this object with the additive inverse of the one specified.
	 * @param other the object to be added
	 * @return this - other. 
	 */
	public A minus(A other);
	
	
	/**
	 * ZERO is defined as this.plus(this.negate)
	 * @return zero
	 */
	public A zero();
}
