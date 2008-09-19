package org.middleheaven.storage.criteria;



public class CriteriaBuilder<T> {

	AbstractCriteria<T> criteria; 
	public static <E> CriteriaBuilder<E> search(Class<E> type) {
		
		CriteriaBuilder<E> builder = new CriteriaBuilder<E>();
		builder.criteria = new AbstractCriteria<E>(type){};
		return builder;
	}

	
	public Criteria<T> all(){
		return this.criteria;
	}
}
