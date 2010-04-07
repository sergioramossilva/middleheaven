package org.middleheaven.storage.criteria;

public class EqualsOtherInstanceCriterion implements Criterion {

	private Object instance;

	public EqualsOtherInstanceCriterion(Object instance) {
		this.instance = instance;
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

}
