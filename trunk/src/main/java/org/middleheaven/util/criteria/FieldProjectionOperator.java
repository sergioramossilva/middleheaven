package org.middleheaven.util.criteria;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.util.criteria.entity.projection.ProjectionOperator;

public abstract class FieldProjectionOperator implements ProjectionOperator {

	private QualifiedName name;
	
	public FieldProjectionOperator(QualifiedName name){
		this.name = name;
	}
	
	public final QualifiedName getFieldName(){
		return name;
	}
}
