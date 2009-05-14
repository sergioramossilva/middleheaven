package org.middleheaven.storage.criteria;

import java.util.Collection;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.Storable;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.Interval;

public class ReflectionFieldFilter<T> implements BooleanClassifier<T> {

	QualifiedName fieldName;
	CriterionOperator operator;
	FieldValueHolder valueHolder;
	
	public ReflectionFieldFilter(QualifiedName fieldName, CriterionOperator operator,FieldValueHolder valueHolder) {
		super();
		this.fieldName = fieldName;
		this.operator = operator;
		this.valueHolder = valueHolder;
	}

	@Override
	public Boolean classify(T obj) {
		
		Storable s = (Storable)obj;
		
		Object realValue = null;
		Object holdedValue = null;
		
		if(valueHolder.getDataType().isReference()){
			
			String targetField = valueHolder.getParam("targetField");
			// TODO acessar o campo directamente
			PropertyAccessor acessor =  ReflectionUtils.getPropertyAccessor(s.getPersistableClass(), targetField );
			
			realValue = acessor.getValue(obj);
			
		} else if (obj!=null){
			PropertyAccessor acessor =  ReflectionUtils.getPropertyAccessor(s.getPersistableClass(), fieldName.getName() );
			
			realValue = acessor.getValue(obj);
	
		} else {
			return false;
		}
		
		holdedValue = valueHolder.getValue();
		boolean result = false;
		if (operator.equals(CriterionOperator.EQUAL) || operator.equals(CriterionOperator.IS)){
			result = holdedValue!=null && holdedValue.equals(realValue);
		} else if (operator.equals(CriterionOperator.IS_NULL)){
			result = realValue == null;
		} else if (operator.equals(CriterionOperator.IN)){
			if (holdedValue instanceof Interval){
				result = ((Interval)holdedValue).contains(realValue);
			} else if (holdedValue instanceof Collection){
				result = ((Collection)holdedValue).contains(realValue);
			}
		} else if (operator.equals(CriterionOperator.GREATER_THAN)){
			result = realValue!=null && ((Comparable)realValue).compareTo(holdedValue) > 0;
		} else if (operator.equals(CriterionOperator.GREATER_THAN_OR_EQUAL)){
			result = realValue!=null && ((Comparable)realValue).compareTo(holdedValue) >= 0;
		} else if (operator.equals(CriterionOperator.LESS_THAN)){
			result = realValue!=null && ((Comparable)realValue).compareTo(holdedValue) < 0;
		} else if (operator.equals(CriterionOperator.LESS_THAN_OR_EQUAL)){
			result = realValue!=null && ((Comparable)realValue).compareTo(holdedValue) <= 0;
		} else if (operator.equals(CriterionOperator.CONTAINS)){
			result = realValue!=null && ((CharSequence)realValue).toString().toLowerCase().indexOf(((CharSequence)holdedValue).toString().toLowerCase()) >=0 ;
		} else if (operator.equals(CriterionOperator.STARTS_WITH)){
			result = realValue!=null && ((CharSequence)realValue).toString().toLowerCase().startsWith(((CharSequence)holdedValue).toString().toLowerCase()) ;
		} else if (operator.equals(CriterionOperator.ENDS_WITH)){
			result = realValue!=null && ((CharSequence)realValue).toString().toLowerCase().endsWith(((CharSequence)holdedValue).toString().toLowerCase()) ;
		} else {
			throw new UnsupportedOperationException(operator.toString() + " not defined");
		}
		
		return result ^ operator.isNegated();
	}

}
