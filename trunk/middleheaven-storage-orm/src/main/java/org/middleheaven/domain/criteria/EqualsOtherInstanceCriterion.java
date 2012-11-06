package org.middleheaven.domain.criteria;

import org.middleheaven.util.criteria.Criterion;

/**
 * 
 */
public class EqualsOtherInstanceCriterion implements Criterion {

	private static final long serialVersionUID = -7511052300807129115L;
	
	private Object instance;

	private Class type;

	/**
	 * 
	 * Constructor.
	 * @param type the type of the instance
	 * @param instance the instance
	 */
	public EqualsOtherInstanceCriterion(Class type, Object instance) {
		this.instance = instance;
		this.type = type;
	}

	public Class getType(){
		return type;
	}
	
	public Object getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Criterion simplify() {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJunction() {
		return false;
	}

}
