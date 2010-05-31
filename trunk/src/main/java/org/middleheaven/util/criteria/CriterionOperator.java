package org.middleheaven.util.criteria;

import java.io.Serializable;

public final class CriterionOperator implements Serializable{
	private static final long serialVersionUID = -909911263154033813L;
	
	public static final CriterionOperator UNKOWN = new CriterionOperator("unkown"); 
	public static final CriterionOperator EQUAL = new CriterionOperator("equals"); 
	public static final CriterionOperator GREATER_THAN = new CriterionOperator("greaterThan"); 
	public static final CriterionOperator GREATER_THAN_OR_EQUAL = new CriterionOperator("greaterThanOrEqual"); 
	public static final CriterionOperator LESS_THAN = new CriterionOperator("lessThan"); 
	public static final CriterionOperator LESS_THAN_OR_EQUAL = new CriterionOperator("lessThanOrEqual"); 
	public static final CriterionOperator IN = new CriterionOperator("in"); 
	public static final CriterionOperator CONTAINS = new CriterionOperator("contains"); 
	public static final CriterionOperator STARTS_WITH = new CriterionOperator("startsWith"); 
	public static final CriterionOperator ENDS_WITH = new CriterionOperator("endsWith"); 
	public static final CriterionOperator IS = new CriterionOperator("is");
	public static final CriterionOperator IS_NULL =  new CriterionOperator("is_null");
	public static final CriterionOperator NEAR =  new CriterionOperator("near");

	
	

	private String name;
	private boolean negated;
	
	private CriterionOperator(String name){
		this(name,false);
	}

	public CriterionOperator(String name, boolean negated) {
		super();
		this.name = name;
		this.negated = negated;
	}


	public CriterionOperator negate() {
		return new CriterionOperator(this.name,!this.negated);
	}

	public boolean equals(Object other) {
		return other instanceof CriterionOperator
				&& equals((CriterionOperator) other);
	}

	public boolean equals(CriterionOperator other) {
		return this.name.equals(other.name);
	}

	public int hashCode() {
		return name.hashCode();
	}	
	
	public String toString(){
		return name;
	}

	public boolean isNegated() {
		return this.negated;
	}
	
}
