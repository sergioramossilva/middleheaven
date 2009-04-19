package org.middleheaven.storage.criteria;

import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.StorableEntityModel;


public class FieldJuntionCriterion implements JuntionCriterion {

	QualifiedName fieldName;
	StorableEntityModel targetEntityModel;
	Criteria<?> subCriteria;
	private String alias;
	StorableEntityModel sourceEntityModel;
	
	public FieldJuntionCriterion(QualifiedName fieldName, StorableEntityModel targetModel, StorableEntityModel  sourceModel){
		if (targetModel == null){
			throw new IllegalArgumentException("Model is required");
		}
		this.fieldName = fieldName;
		this.targetEntityModel = targetModel;
		this.sourceEntityModel = sourceModel;
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

	public StorableEntityModel getTargetEntityModel() {
		return targetEntityModel;
	}

	public void setAlias(String targetAlias) {
		this.alias = targetAlias;
	}


	public String getAlias() {
		return alias;
	}

	public StorableEntityModel getSourceEntityModel() {
		return sourceEntityModel;
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
