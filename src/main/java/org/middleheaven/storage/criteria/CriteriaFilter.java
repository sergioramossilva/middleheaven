package org.middleheaven.storage.criteria;

import org.middleheaven.classification.BooleanClassifier;

public class CriteriaFilter<T> extends AbstractCriteria<T> implements BooleanClassifier<T>{

	public CriteriaFilter(Criteria<T> other) {
		super(other.getTargetClass());
		//TODO
	}

	@Override
	public Boolean classify(T obj) {
		// TODO Auto-generated method stub
		return true;
	}


}
