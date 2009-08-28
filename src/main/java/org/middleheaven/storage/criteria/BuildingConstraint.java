/**
 * 
 */
package org.middleheaven.storage.criteria;

import java.util.Arrays;
import java.util.Collection;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.util.collections.Interval;

class BuildingConstraint<T,B extends AbstractCriteriaBuilder<T, B>> implements Constraint<T, B> {

	private QualifiedName qname;
	private B builder;
	private boolean negateFlag = false;

	public BuildingConstraint (B builder,QualifiedName qualifiedFileName){
		this.qname = qualifiedFileName;
		this.builder = builder;
	}

	private B constrainField(CriterionOperator op, Object value){
		if (negateFlag){
			negateFlag=false;
			op = op.negate();
		}

		builder.addCriterion(
				new UniqueFieldCriterion(
						qname,
						op,
						new SingleObjectValueHolder(value,null)
				)
		);
		return builder;
	}

	@Override
	public <V> B in(Collection<V> values) {
		CriterionOperator op = CriterionOperator.IN;
		if (negateFlag){
			negateFlag=false;
			op = op.negate();
		}

		builder.addCriterion(
				new CollectionFieldInSetCriteria(
						qname,
						op,
						values
				)
		);
		return builder;

	}

	@Override
	public Constraint<T,B> not() {
		this.negateFlag = !this.negateFlag;
		return this;
	}

	@Override
	public B eq(Object value) {
		return constrainField(CriterionOperator.EQUAL,value);
	}

	@Override
	public B ge(Object value) {
		return constrainField(CriterionOperator.GREATER_THAN_OR_EQUAL,value);
	}

	@Override
	public B gt(Object value) {
		return constrainField(CriterionOperator.GREATER_THAN,value);
	}

	@Override
	public B le(Object value) {
		return constrainField(CriterionOperator.LESS_THAN_OR_EQUAL,value);
	}

	@Override
	public B lt(Object value) {
		return constrainField(CriterionOperator.LESS_THAN,value);
	}

	@Override
	public B isNull() {
		return eq(null);
	}

	@Override
	public <V> B in(V... values) {
		return in(Arrays.asList(values));
	}


	@Override
	public <V extends Comparable<? super V>> B bewteen(V min, V max) {
		return in(Interval.between(min, max));
	}


	@Override
	public <V extends Comparable<? super V>> B in(Interval<V> interval) {
		return constrainField(CriterionOperator.IN, interval);

	}

	@Override
	public B contains(CharSequence text) {
		return constrainField(CriterionOperator.CONTAINS, text);
	}

	@Override
	public B endsWith(CharSequence text) {
		return constrainField(CriterionOperator.ENDS_WITH, text);
	}

	@Override
	public B startsWith(CharSequence text) {
		return constrainField(CriterionOperator.STARTS_WITH, text);
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
		FieldJuntionCriterion criterion = new FieldJuntionCriterion(qname,referencedEntityType,builder.getCurrentType());
		builder.addCriterion(criterion);
		return new JunctionCriteriaBuilder<N,T,B>(criterion,referencedEntityType,builder);
	}




}