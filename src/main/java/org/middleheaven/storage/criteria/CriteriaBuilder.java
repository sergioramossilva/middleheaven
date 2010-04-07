package org.middleheaven.storage.criteria;

import org.middleheaven.util.identity.Identity;



/**
 * An implementation of the
 * <pattern>Builder</pattern> pattern with method chaining
 * to help build a <code>Criteria<T></code> object 
 *
 * @param <T> current object type for the application of restrictions
 * @param <Parent> object type for the resulting criteria.
 */
public class CriteriaBuilder<T> extends AbstractCriteriaBuilder<T, CriteriaBuilder<T>>{

	public static <L> CriteriaBuilder<L> search(Class<L> type) {
		return new CriteriaBuilder<L>(type);
	}
	
	protected CriteriaBuilder(Class<T> type){
		super(type);
	}

	public Criteria<T> distinct(){
		this.criteria.setDistinct(true);
		return this.criteria;
	}
	
	public Criteria<T> all() {
		return criteria;
	}

	/* TODO Can Projections be set only in the criteria it self when needed  
	public CriteriaBuilder<T> sum(String qname){
		this.criteria.add(Projections.sum(QualifiedName.of(qname)));
		return this;
	}

	public CriteriaBuilder<T> max(String qname){
		this.criteria.add(Projections.max(QualifiedName.of(qname)));
		return this;
	}

	 */

	public CriteriaBuilder<T> limit(int count) {
		this.criteria.setRange(count);
		return this;
	}

	public CriteriaBuilder<T> limit(int start, int count) {
		this.criteria.setRange(start,count);
		return this;
	}



}
