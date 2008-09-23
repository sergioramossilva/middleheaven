package org.middleheaven.storage.criteria;

public interface OrderingConstrain<T> {

	public CriteriaBuilder<T>  asc();
	public CriteriaBuilder<T> desc();
}
