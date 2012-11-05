package org.middleheaven.util.criteria;

public final class EmptyCriterion implements Criterion{


	private static final long serialVersionUID = -5297475726331343866L;
	private static final EmptyCriterion ME = new EmptyCriterion();
	
	public static EmptyCriterion empty(){
		return ME;
	}
	
	@Override
	public boolean isEmpty() {
		return true;
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
