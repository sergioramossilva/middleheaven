package org.middleheaven.storage.criteria;

import java.io.Serializable;

public interface Criterion extends Serializable{

	public boolean isEmpty();

	public Criterion simplify();

	/**
	 * 
	 * @return apply a not operator to the criterion.
	 * @throws <code>java.lang.UnsupportedOperationException</code> if this <code>Criterion</code> cannot be negated
	 */
	public Criterion negate();
}
