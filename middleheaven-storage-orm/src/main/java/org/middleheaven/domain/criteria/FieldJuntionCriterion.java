package org.middleheaven.domain.criteria;

import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.criteria.Criteria;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.CriterionOperator;
import org.middleheaven.util.criteria.FieldValueHolder;
import org.middleheaven.util.criteria.JunctionCriterion;

/**
 * Junction criterion based on a field.
 */
public class FieldJuntionCriterion implements JunctionCriterion {

	private static final long serialVersionUID = -348614713682815750L;
	
	private QualifiedName fieldName;
	private Class<?> targetType;
	private Class<?> sourceType;
	private Criteria<?> subCriteria;
	private String alias;
	private boolean reversed;

	/**
	 * 
	 * @param fieldName the field's name
	 * @param targetType the target junction type
	 * @param sourceType the source junction type
	 * @param reversed if the junction is reversed.
	 */
	 public FieldJuntionCriterion(QualifiedName fieldName, Class<?> targetType, Class<?>  sourceType, boolean reversed){
		if (targetType == null){
			throw new IllegalArgumentException("Target type is required");
		}
		this.fieldName = fieldName;
		this.targetType = targetType;
		this.sourceType = sourceType;
		this.reversed = reversed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJunction() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSubCriteria(Criteria<?> criteria) {
		this.subCriteria = criteria;
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

	@Override
	public boolean isReversed() {
		return this.reversed;
	}


}
