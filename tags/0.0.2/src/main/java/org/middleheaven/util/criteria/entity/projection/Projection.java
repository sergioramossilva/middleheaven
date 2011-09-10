package org.middleheaven.util.criteria.entity.projection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.util.criteria.entity.EntityCriteria;

public class Projection<P> {
	
	private final List<GroupCriterion> groups = new LinkedList<GroupCriterion>();
	private final List<ProjectionOperator> operators = new LinkedList<ProjectionOperator>();
	private final EntityCriteria<?> criteria;
	private Class<P> resultType;
	
	public Projection(EntityCriteria<?> criteria) {
		this.criteria = criteria;
	}

	public Collection<GroupCriterion> groups() {
		return groups;
	}
	
	public Collection<ProjectionOperator> operators() {
		return operators;
	}

	public void add(ProjectionOperator operator) {
		operators.add(operator);
	}

	public void addGroupCriterion(GroupCriterion fieldGroupCriterion) {
		groups.add(fieldGroupCriterion);
	}
	
	/**
	 * 
	 * @return the criteria to project
	 */
	public EntityCriteria<?> getCriteria(){
		return this.criteria;
	}
	
	public Class<P> getProjectionResultType(){
		return resultType;
	}
	
	public void setProjectionResultType(Class<P> resultType){
		this.resultType = resultType;
	}

}
