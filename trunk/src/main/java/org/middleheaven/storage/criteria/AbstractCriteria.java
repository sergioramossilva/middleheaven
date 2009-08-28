package org.middleheaven.storage.criteria;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.util.classification.LogicOperator;


public abstract class AbstractCriteria <T> implements Criteria<T>{

	private Class<T> targetClass;
	private Class<T> fromClass;
	
	private boolean keyOnly = false;
	private boolean countOnly = false;
	private boolean distinct;
	private int count = -1;
	private int start = -1;
	private LogicCriterion restrictions = new LogicCriterion(LogicOperator.and());
	private List<OrderingCriterion> ordering = new LinkedList<OrderingCriterion>();
	private Projection aggregation;
	private List<QualifiedName> resultFields = new LinkedList<QualifiedName>();
	
	
    AbstractCriteria(Class<T> targetClass){
		this.targetClass = targetClass;
	}
	
	protected AbstractCriteria(AbstractCriteria<T> other){
		this.targetClass = other.targetClass;
		this.keyOnly = other.keyOnly;
		this.distinct = other.distinct;
		this.count = other.count;
		this.start = other.start;
		
		this.restrictions = (LogicCriterion)other.restrictions.clone();
		this.ordering = new LinkedList<OrderingCriterion>(other.ordering);
		this.resultFields = new LinkedList<QualifiedName>(other.resultFields);
		
	}
	
	protected void setRestrictions(LogicCriterion restrictions){
		this.restrictions = restrictions;
	}
	


    
	public List<OrderingCriterion> ordering(){
		return ordering;
	}
	
	public LogicCriterion constraints(){
		return restrictions;
	}
	
	public Class<T> getTargetClass(){
		return targetClass; 
	}
	
	public Class<T> getFromClass(){
		return fromClass; 
	}
	
	public Criteria<T> add(Criterion criterion){
		this.restrictions.add(criterion);
		return this;
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

	public void setKeyOnly(boolean keyOnly) {
		this.keyOnly = keyOnly;
	}
	
	public final boolean isKeyOnly(){
		return this.keyOnly;
	}

	@Override
	public Criteria<T> add(OrderingCriterion order){
		this.ordering.add(order);
		return this;
	}
	
	@Override
	public Collection<QualifiedName> resultFields() {
		// return not transient fields for result
		return resultFields;
	}

	public void add(ProjectionOperator operator) {
		this.aggregation.add(operator);
	}

	@Override
	public boolean isCountOnly() {
		return countOnly;
	}

	@Override
	public void setCountOnly(boolean countOnly) {
		this.countOnly= countOnly;
	}
}
