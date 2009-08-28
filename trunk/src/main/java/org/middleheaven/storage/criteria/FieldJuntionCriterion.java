package org.middleheaven.storage.criteria;

import org.middleheaven.storage.QualifiedName;


public class FieldJuntionCriterion implements JunctionCriterion {

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
	
	@Override
	public void setSubCriteria(Criteria<?> subCriteria){
		this.subCriteria = subCriteria;
	}
	
	@Override
	public Criteria<?> getSubCriteria(){
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

	@Override
	public void setAlias(String targetAlias) {
		this.alias = targetAlias;
	}

	@Override
	public String getAlias() {
		return alias;
	}
	@Override
	public Class<?> getTargetType() {
		return this.targetType;
	}
	@Override
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
