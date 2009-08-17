package org.middleheaven.storage.criteria;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableEntityModel;


public class FieldJuntionCriterion implements JuntionCriterion {

	QualifiedName fieldName;
	Class<?> targetType;
	Class<?> sourceType;
	Criteria<?> subCriteria;
	private String alias;


	public FieldJuntionCriterion(QualifiedName fieldName, Class<?> targetType, Class<?>  sourceType){
		if (targetType == null){
			throw new IllegalArgumentException("Target type is required");
		}
		this.fieldName = fieldName;
		this.targetType = targetType;
		this.sourceType = sourceType;
	}

	public void setSubCriteria(Criteria subCriteria){
		this.subCriteria = subCriteria;
	}

	public Criteria getSubCriteria(){
		return this.subCriteria;
	}

	@Override
	public QualifiedName getFieldName() {
		return fieldName;
	}

	@Override
	public CriterionOperator getOperator() {
		return CriterionOperator.EQUAL;
	}

	@Override
	public FieldValueHolder valueHolder() {
		return null;
	}

	public void setAlias(String targetAlias) {
		this.alias = targetAlias;
	}


	public String getAlias() {
		return alias;
	}
	
	public Class<?> getTargetType() {
		return this.targetType;
	}
	
	public Class<?> getSourceType() {
		return this.sourceType;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Criterion simplify() {
		return this;
	}
}
