package org.middleheaven.storage.criteria;

public class IdentityCriterion implements Criterion {

	
	private Object identity;

	public IdentityCriterion(Object identity){
		this.identity = identity;
	}
	
	
	public Object getIdentity(){
		return identity;
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
