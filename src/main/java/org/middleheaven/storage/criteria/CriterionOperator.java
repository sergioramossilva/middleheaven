package org.middleheaven.storage.criteria;

public enum CriterionOperator {

	EQUAL, 
	NOT_EQUAL, 
	MATCH,
	NOT_MATCH,
	IN,
	NOT_IN,
	GREATER_THAN, 
	GREATER_THAN_OR_EQUAL,
	LESS_THAN, 
	LESS_THAN_OR_EQUAL, 
	IS_NULL,
	IS_NOT_NULL,
	UNKOWN;

	public CriterionOperator negate(){
		switch (this){
		case EQUAL:
			return NOT_EQUAL;
		case NOT_EQUAL:
			return EQUAL;
		case MATCH:
			return NOT_MATCH;
		case NOT_MATCH:
			return MATCH;
		case IN:
			return NOT_IN;
		case NOT_IN:
			return IN;
		case GREATER_THAN:
			return LESS_THAN_OR_EQUAL;
		case LESS_THAN_OR_EQUAL:
			return GREATER_THAN;
		case GREATER_THAN_OR_EQUAL:
			return LESS_THAN;
		case LESS_THAN:
			return GREATER_THAN_OR_EQUAL;
		case IS_NULL:
			return IS_NOT_NULL;
		case IS_NOT_NULL:
			return IS_NULL;
		default:
			return UNKOWN;
		}
	}

}
