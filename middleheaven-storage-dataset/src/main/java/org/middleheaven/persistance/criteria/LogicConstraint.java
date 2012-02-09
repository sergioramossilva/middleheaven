/**
 * 
 */
package org.middleheaven.persistance.criteria;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.util.classification.LogicOperator;

/**
 * 
 */
public class LogicConstraint implements DataSetConstraint {

	
	private final LogicOperator operator;
	private final List<DataSetConstraint> constraints;

	public LogicConstraint(LogicConstraint other){
		this.operator = other.operator;
		this.constraints = new ArrayList<DataSetConstraint>(other.constraints);
	}

	public LogicConstraint(LogicOperator operator){
		this.operator = operator;
		this.constraints = new ArrayList<DataSetConstraint>(5);
	}
	
	public LogicOperator getOperator() {
		return operator;
	}
	
	public List<DataSetConstraint> getConstraints(){
		return constraints;
	}
	
	public void addConstraint (DataSetConstraint column){
		constraints.add(column);
	}

	public void removeConstraint (DataSetConstraint column){
		constraints.remove(column);
	}
	
	public boolean isEmpty() {
		return this.constraints.isEmpty();
	}
	
	public LogicConstraint simplify(){
		LogicConstraint simple = new LogicConstraint(this.operator);
		
		for (DataSetConstraint c : this.constraints){
			if(!c.isEmpty()){
				simple.constraints.add(c);
			}
		}
		
		return simple;
	}

}
