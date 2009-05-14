package org.middleheaven.storage.criteria;

import java.util.Arrays;
import java.util.Collection;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.util.collections.Interval;

/**
 * An implementation of the
 * <pattern>Builder</pattern> pattern with Fluent Interface
 * to help build a <code>Criteria<T></code> object 
 *
 * @param <T> result object for the criteria
 */
public final class CriteriaBuilder<T> {

	public static <L> CriteriaBuilder<L> search(Class<L> type) {
		return new CriteriaBuilder<L>(type);
	}

	private AbstractCriteria<T> criteria;

	private CriteriaBuilder(Class<T> type){
		this.criteria = new BuildedCriteria<T>(type);
	}
	
	private static class BuildedCriteria<T> extends AbstractCriteria<T>{

		public BuildedCriteria(BuildedCriteria<T> other) {
			super(other);
		}

		public BuildedCriteria(Class<T> type) {
			super(type);
		}

		@Override
		public Criteria<T> duplicate() {
			return new BuildedCriteria<T>(this);
		}
		
	}
	
	public Criteria<T> distinct(){
		this.criteria.setDistinct(true);
		return this.criteria;
	}

	public Criteria<T> all(){
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

	public CriteriaBuilder<T> isEqual(T instance){
		new BuildingConstraint<T>(this,QualifiedName.qualify(this.criteria.getTargetClass().getSimpleName().toLowerCase(), "identity"))
		.eq(instance);
		return this;
	}

	public CriteriaBuilder<T> isSame(T instance){
		// TODO read and set unique identifiers
		new BuildingConstraint<T>(this,QualifiedName.qualify(this.criteria.getTargetClass().getSimpleName().toLowerCase(), "identity"))
		.eq(instance);
		return this;
	}

	public Constraint<T> and(String name) {
		return new BuildingConstraint<T>(this,QualifiedName.qualify(this.criteria.getTargetClass().getSimpleName().toLowerCase(), name));
	}

	public Constraint<T> or(String name) {
		return new BuildingConstraint<T>(this,QualifiedName.qualify(this.criteria.getTargetClass().getSimpleName().toLowerCase(), name));
	}

	public CriteriaBuilder<T> limit(int count) {
		this.criteria.setRange(count);
		return this;
	}

	public CriteriaBuilder<T> limit(int start, int count) {
		this.criteria.setRange(start,count);
		return this;
	}

	public OrderingConstrain<T> orderBy(String name) {
		return new BuildingOrdering<T>(this,QualifiedName.qualify(this.criteria.getTargetClass().getSimpleName().toLowerCase(), name));
	}


	private void addCriterion(Criterion criterion){
		this.criteria.add(criterion);
	}

	private static class BuildingOrdering<X> implements OrderingConstrain<X>{

		private QualifiedName qname;
		private CriteriaBuilder<X> builder;

		public BuildingOrdering (CriteriaBuilder<X> builder,QualifiedName qualifiedFileName){
			this.qname = qualifiedFileName;
			this.builder = builder;
		}

		@Override
		public CriteriaBuilder<X> asc() {
			builder.criteria.add(OrderingCriterion.asc(qname));
			return builder;
		}

		@Override
		public CriteriaBuilder<X> desc() {
			builder.criteria.add(OrderingCriterion.desc(qname));
			return builder;
		}

	}

	private static class BuildingConstraint<X> implements Constraint<X> {

		private QualifiedName qname;
		private CriteriaBuilder<X> builder;
		private boolean negateFlag = false;

		public BuildingConstraint (CriteriaBuilder<X> builder,QualifiedName qualifiedFileName){
			this.qname = qualifiedFileName;
			this.builder = builder;
		}

		private CriteriaBuilder<X> constrainField(CriterionOperator op, Object value){
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
		public <V> CriteriaBuilder<X> in(Collection<V> values) {
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
		public Constraint<X> not() {
			this.negateFlag = !this.negateFlag;
			return this;
		}

		@Override
		public CriteriaBuilder<X> eq(Object value) {
			return constrainField(CriterionOperator.EQUAL,value);
		}

		@Override
		public CriteriaBuilder<X> ge(Object value) {
			return constrainField(CriterionOperator.GREATER_THAN_OR_EQUAL,value);
		}

		@Override
		public CriteriaBuilder<X> gt(Object value) {
			return constrainField(CriterionOperator.GREATER_THAN,value);
		}

		@Override
		public CriteriaBuilder<X> le(Object value) {
			return constrainField(CriterionOperator.LESS_THAN_OR_EQUAL,value);
		}

		@Override
		public CriteriaBuilder<X> lt(Object value) {
			return constrainField(CriterionOperator.LESS_THAN,value);
		}

		@Override
		public CriteriaBuilder<X> isNull() {
			return eq(null);
		}

		@Override
		public <V> CriteriaBuilder<X> in(V... values) {
			return in(Arrays.asList(values));
		}

	
		@Override
		public <V extends Comparable<? super V>> CriteriaBuilder<X> bewteen(V min, V max) {
			return in(Interval.between(min, max));
		}


		@Override
		public <V extends Comparable<? super V>> CriteriaBuilder<X> in(Interval<V> interval) {
			return constrainField(CriterionOperator.IN, interval);

		}

		@Override
		public CriteriaBuilder<X> contains(CharSequence text) {
			return constrainField(CriterionOperator.CONTAINS, text);
		}

		@Override
		public CriteriaBuilder<X> endsWith(CharSequence text) {
			return constrainField(CriterionOperator.ENDS_WITH, text);
		}

		@Override
		public CriteriaBuilder<X> startsWith(CharSequence text) {
			return constrainField(CriterionOperator.STARTS_WITH, text);
		}




	}


}
