package org.middleheaven.storage.criteria;

import org.middleheaven.storage.QualifiedName;

public final class Projections {

	private Projections(){}

	public static ProjectionOperator count(){
		return new CountOperator(null); // count (*)
	}

	public static ProjectionOperator count(QualifiedName fieldName){
		return new CountOperator(fieldName); // count (fieldName)
	}

	public static ProjectionOperator sum(QualifiedName fieldName){
		return new SumFieldOperator(fieldName); // sum (fieldName)
	}

	public static ProjectionOperator max(QualifiedName fieldName){
		return new MaxFieldOperator(fieldName);
	}
}
