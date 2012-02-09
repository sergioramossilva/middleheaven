//package org.middleheaven.domain.criteria;
//
//import java.util.Collection;
//
//import org.middleheaven.core.reflection.PropertyAccessor;
//import org.middleheaven.domain.store.EntityInstance;
//import org.middleheaven.util.classification.BooleanClassifier;
//import org.middleheaven.util.collections.Interval;
//import org.middleheaven.util.criteria.CriterionOperator;
//import org.middleheaven.util.criteria.FieldValueHolder;
//import org.middleheaven.util.criteria.QualifiedName;
//
///**
// * 
// * @param <T>
// */
//public class ReflectionFieldFilter<T> implements BooleanClassifier<T> {
//
//	private final QualifiedName fieldName;
//	private final CriterionOperator operator;
//	private final FieldValueHolder valueHolder;
//	
//	/**
//	 * 
//	 * Constructor.
//	 * @param fieldName
//	 * @param operator
//	 * @param valueHolder
//	 */
//	public ReflectionFieldFilter(QualifiedName fieldName, CriterionOperator operator, FieldValueHolder valueHolder) {
//		super();
//		this.fieldName = fieldName;
//		this.operator = operator;
//		this.valueHolder = valueHolder;
//	}
//
//	@Override
//	public Boolean classify(T obj) {
//		
//		EntityInstance s = (EntityInstance)obj;
//		
//		Object realValue = null;
//		Object holdedValue = null;
//		
//		if(valueHolder.getDataType().isReference()){
//			
//			String targetField = valueHolder.getParam("targetField");
//	
//			PropertyAccessor acessor = s.getEntityModel().getEntityClass().getPropertyAcessor(targetField);
//	
//			realValue = acessor.getValue(obj);
//			
//		} else if (obj!=null){
//			PropertyAccessor acessor = s.getEntityModel().getEntityClass().getPropertyAcessor(fieldName.getName());
//
//			realValue = acessor.getValue(obj);
//	
//		} else {
//			return false;
//		}
//		
//		holdedValue = valueHolder.getValue();
//		boolean result = false;
//		if (operator.equals(CriterionOperator.EQUAL) || operator.equals(CriterionOperator.IS)){
//			result = holdedValue!=null && holdedValue.equals(realValue);
//		} else if (operator.equals(CriterionOperator.IS_NULL)){
//			result = realValue == null;
//		} else if (operator.equals(CriterionOperator.IN)){
//			if (holdedValue instanceof Interval){
//				result = ((Interval)holdedValue).contains(realValue);
//			} else if (holdedValue instanceof Collection){
//				result = ((Collection)holdedValue).contains(realValue);
//			}
//		} else if (operator.equals(CriterionOperator.GREATER_THAN)){
//			result = realValue!=null && ((Comparable)realValue).compareTo(holdedValue) > 0;
//		} else if (operator.equals(CriterionOperator.GREATER_THAN_OR_EQUAL)){
//			result = realValue!=null && ((Comparable)realValue).compareTo(holdedValue) >= 0;
//		} else if (operator.equals(CriterionOperator.LESS_THAN)){
//			result = realValue!=null && ((Comparable)realValue).compareTo(holdedValue) < 0;
//		} else if (operator.equals(CriterionOperator.LESS_THAN_OR_EQUAL)){
//			result = realValue!=null && ((Comparable)realValue).compareTo(holdedValue) <= 0;
//		} else if (operator.equals(CriterionOperator.CONTAINS)){
//			result = realValue!=null && ((CharSequence)realValue).toString().toLowerCase().indexOf(((CharSequence)holdedValue).toString().toLowerCase()) >=0 ;
//		} else if (operator.equals(CriterionOperator.STARTS_WITH)){
//			result = realValue!=null && ((CharSequence)realValue).toString().toLowerCase().startsWith(((CharSequence)holdedValue).toString().toLowerCase()) ;
//		} else if (operator.equals(CriterionOperator.ENDS_WITH)){
//			result = realValue!=null && ((CharSequence)realValue).toString().toLowerCase().endsWith(((CharSequence)holdedValue).toString().toLowerCase()) ;
//		} else {
//			throw new UnsupportedOperationException(operator.toString() + " not defined");
//		}
//		
//		return result ^ operator.isNegated();
//	}
//
//}
