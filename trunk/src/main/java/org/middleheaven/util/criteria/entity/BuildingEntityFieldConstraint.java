/**
 * 
 */
package org.middleheaven.util.criteria.entity;

import java.util.Arrays;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.util.criteria.AbstractBuildingConstraint;

class BuildingEntityFieldConstraint<T,B extends AbstractEntityCriteriaBuilder<T, B>> 
extends AbstractBuildingConstraint<T,B> 
implements EntityFieldConstraint<T, B> {

	protected BuildingEntityFieldConstraint(B builder,QualifiedName qualifiedFileName) {
		super(builder, qualifiedFileName);
	}


	@Override
	public EntityFieldConstraint<T,B> not() {
		this.toogleNegate();
		return this;
	}

	@Override
	public <V> B in(V... values) {
		return in(Arrays.asList(values));
	}

	@Override
	public <O> B is(O candidate) {
		if (candidate == null){
			return this.isNull();
		} else {
			return navigateTo(Introspector.of(candidate).getRealType())
			.isEqual(candidate)
			.back();
		}
	}

	@Override
	public <N> JunctionCriteriaBuilder<N, T , B> navigateTo(Class<N> referencedEntityType) {
		FieldJuntionCriterion criterion = new FieldJuntionCriterion(this.getQualifiedName(),referencedEntityType,getBuilder().getCurrentType());
		getBuilder().addCriterion(criterion);
		return new JunctionCriteriaBuilder<N,T,B>(criterion,referencedEntityType,getBuilder());
	}




}