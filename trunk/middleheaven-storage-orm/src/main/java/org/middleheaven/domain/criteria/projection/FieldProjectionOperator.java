package org.middleheaven.domain.criteria.projection;

import org.middleheaven.util.QualifiedName;


public abstract class FieldProjectionOperator implements ProjectionOperator {

	private QualifiedName name;
	
	public FieldProjectionOperator(QualifiedName name){
		this.name = name;
	}
	
	public final QualifiedName getFieldName(){
		return name;
	}
}
