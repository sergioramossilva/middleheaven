package org.middleheaven.storage.criteria;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.classification.LogicOperator;
import org.middleheaven.storage.QualifiedName;


abstract class AbstractCriteria <T> implements Criteria<T>{

	private Class<T> targetClass;
	private Class<T> fromClass;
	
	private boolean distinct;
	private int count = -1;
	private int start = -1;
	private LogicCriterion restrictions = new LogicCriterion(LogicOperator.and());
	private List<OrderingCriterion> ordering = new LinkedList<OrderingCriterion>();
	private Projection aggregation;
	public List resultFields;
	

    AbstractCriteria(Class<T> targetClass){
		this.targetClass = targetClass;
	}
	
	public List<OrderingCriterion> ordering(){
		return ordering;
	}
	
	public LogicCriterion restrictions(){
		return restrictions;
	}
	
	
	public Class<T> getTargetClass(){
		return targetClass; 
	}
	
	public Class<T> getFromClass(){
		return fromClass; 
	}
	
	public void add(Criterion criterion){
		this.restrictions.add(criterion);
	}

	public boolean isDistinct() {
		return distinct;
	}

	public Criteria<T> setDistinct(boolean distinct) {
		this.distinct = distinct;
		return this;
	}


	public int getCount() {
		return count;
	}

	public int getStart() {
		return start;
	}

	public Projection projection() {
		return aggregation;
	}

	public Criteria<T> setRange(int count) {
		this.count = count;
		return this;
	}
	
	public Criteria<T> setRange(int start, int count) {
		this.start = start;
		this.count = count;
		return this;
	}

	public void setKeyOnly(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Criteria<T> add(OrderingCriterion order){
		this.ordering.add(order);
		return this;
	}
	
	@Override
	public Collection<QualifiedName> resultFields() {
		// return not transient fields for result
	};
}
