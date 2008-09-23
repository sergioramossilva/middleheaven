package org.middleheaven.storage.criteria;

public final class EmptyCriterion implements Criterion{


	private static final long serialVersionUID = -5297475726331343866L;
	private static final EmptyCriterion me = new EmptyCriterion();
	
	public static EmptyCriterion empty(){
		return me;
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public Criterion simplify() {
		return this;
	}


}
