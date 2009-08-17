/**
 * 
 */
package org.middleheaven.storage.criteria;

import org.middleheaven.storage.QualifiedName;

class BuildingOrdering<T,B extends CriteriaBuilderStrategy<T,B>> implements OrderingConstrain<T,B>{

	private QualifiedName qname;
	private B builder;


	public BuildingOrdering (B builder,QualifiedName qualifiedFileName){
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