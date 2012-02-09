package org.middleheaven.util.criteria.text;

import org.middleheaven.text.indexing.IndexableDocument;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.criteria.AbstractCriteriaBuilder;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.OrderingCriterion;

public class AbstractTextCriteriaBuilder <D extends IndexableDocument, B extends AbstractTextCriteriaBuilder<D,B>> 
 extends AbstractCriteriaBuilder<D,B> {

	
	private TextSearchCriteria<D> criteria;
	
	public AbstractTextCriteriaBuilder(Class<D> type) {
		this.criteria = new AbstractTextCriteria<D>(type);
	}

	@Override
	protected void addOrderingCriterion(OrderingCriterion criterion) {
		//TODO not implement yet
	}

	@Override
	protected void addCriterion(Criterion criterion) {
		this.criteria.add(criterion);
	}
	
	public final TextSearchCriteria<D> all() {
		return criteria;
	}

	public final TextFieldConstraint<D, B> and(String name) {
		return new BuildingTextFieldConstraint<D, B>(me(), QualifiedName.qualify("", name));
	}

	private B me() {
		return (B) this;
	}

}
