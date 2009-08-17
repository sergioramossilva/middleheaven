/**
 * 
 */
package org.middleheaven.storage.criteria;

import java.util.Arrays;
import java.util.Collection;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.util.collections.Interval;

class BuildingConstraint<T> implements Constraint<T, CriteriaBuilder<T>> {

	private QualifiedName qname;
	private CriteriaBuilder<T> builder;
	private boolean negateFlag = false;

	public BuildingConstraint (CriteriaBuilder<T> builder,QualifiedName qualifiedFileName){
		this.qname = qualifiedFileName;
		this.builder = builder;
	}

	private CriteriaBuilder<T> constrainField(CriterionOperator op, Object value){
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
	public <V> CriteriaBuilder<T> in(Collection<V> values) {
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
	public Constraint<T,CriteriaBuilder<T>> not() {
		this.negateFlag = !this.negateFlag;
		return this;
	}

	@Override
	public CriteriaBuilder<T> eq(Object value) {
		return constrainField(CriterionOperator.EQUAL,value);
	}

	@Override
	public CriteriaBuilder<T> ge(Object value) {
		return constrainField(CriterionOperator.GREATER_THAN_OR_EQUAL,value);
	}

	@Override
	public CriteriaBuilder<T> gt(Object value) {
		return constrainField(CriterionOperator.GREATER_THAN,value);
	}

	@Override
	public CriteriaBuilder<T> le(Object value) {
		return constrainField(CriterionOperator.LESS_THAN_OR_EQUAL,value);
	}

	@Override
	public CriteriaBuilder<T> lt(Object value) {
		return constrainField(CriterionOperator.LESS_THAN,value);
	}

	@Override
	public CriteriaBuilder<T> isNull() {
		return eq(null);
	}

	@Override
	public <V> CriteriaBuilder<T> in(V... values) {
		return in(Arrays.asList(values));
	}


	@Override
	public <V extends Comparable<? super V>> CriteriaBuilder<T> bewteen(V min, V max) {
		return in(Interval.between(min, max));
	}


	@Override
	public <V extends Comparable<? super V>> CriteriaBuilder<T> in(Interval<V> interval) {
		return constrainField(CriterionOperator.IN, interval);

	}

	@Override
	public CriteriaBuilder<T> contains(CharSequence text) {
		return constrainField(CriterionOperator.CONTAINS, text);
	}

	@Override
	public CriteriaBuilder<T> endsWith(CharSequence text) {
		return constrainField(CriterionOperator.ENDS_WITH, text);
	}

	@Override
	public CriteriaBuilder<T> startsWith(CharSequence text) {
		return constrainField(CriterionOperator.STARTS_WITH, text);
	}

	@Override
	public <O> CriteriaBuilder<T> is(O candidate) {
		if (candidate == null){
			return this.isNull();
		} else {
			return navigateTo(Introspector.of(candidate).getType())
			.isEqual(candidate)
			.back();
		}
	}

	@Override
	public <N> ReferenceCriteriaBuilder<N, T , CriteriaBuilder<T>> navigateTo(Class<N> referencedEntityType) {

		builder.criteria.add(new FieldJuntionCriterion(qname,referencedEntityType,builder.criteria.getFromClass()));
		return new ReferenceCriteriaBuilder<N,T,CriteriaBuilder<T>>(referencedEntityType,builder);
	}




}