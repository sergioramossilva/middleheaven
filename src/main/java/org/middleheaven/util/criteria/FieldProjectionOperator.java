package org.middleheaven.util.criteria;

import org.middleheaven.domain.store.QualifiedName;
import org.middleheaven.util.criteria.entity.ProjectionOperator;

public abstract class FieldProjectionOperator implements ProjectionOperator {

	QualifiedName name;
	public FieldProjectionOperator(QualifiedName name){
		this.name = name;
	}
	
	public final QualifiedName getFieldName(){
		return name;
	}
}
