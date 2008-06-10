package org.middleheaven.storage.criteria;

import org.middleheaven.storage.QualifiedName;

public abstract class FieldProjectionOperator implements ProjectionOperator {

	QualifiedName name;
	public FieldProjectionOperator(QualifiedName name){
		this.name = name;
	}
	
	public final QualifiedName getFieldName(){
		return name;
	}
}
