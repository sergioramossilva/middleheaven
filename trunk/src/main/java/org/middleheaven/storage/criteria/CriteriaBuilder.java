package org.middleheaven.storage.criteria;



public class CriteriaBuilder {

	public static <T> Criteria<T> createCriteria(Class<T> type) {
		return new AbstractCriteria<T>(type){};
	}

}
