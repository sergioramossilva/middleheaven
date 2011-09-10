/**
 * 
 */
package org.middleheaven.util.criteria;

import org.middleheaven.storage.QualifiedName;

public class BuildingOrdering<T,B extends AbstractCriteriaBuilder<T,B>> implements OrderingConstrain<T, B>{

	private QualifiedName qname;
	private B builder;


	public BuildingOrdering (B builder, QualifiedName qualifiedFileName){
		this.qname = qualifiedFileName;
		this.builder = builder;
	}

	@Override
	public B asc() {
		builder.addOrderingCriterion(OrderingCriterion.asc(qname));
		return builder;
	}

	@Override
	public B desc() {
		builder.addOrderingCriterion(OrderingCriterion.desc(qname));
		return builder;
	}

}