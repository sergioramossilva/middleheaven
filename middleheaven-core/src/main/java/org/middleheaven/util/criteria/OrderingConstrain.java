package org.middleheaven.util.criteria;


public interface OrderingConstrain<T ,  B extends AbstractCriteriaBuilder<T, B>> {

	/**
	 * Add ascending order criterion
	 * @return
	 */
	public B  asc();
	
	/**
	 * add descending order criterion
	 * @return
	 */
	public B desc();
}
