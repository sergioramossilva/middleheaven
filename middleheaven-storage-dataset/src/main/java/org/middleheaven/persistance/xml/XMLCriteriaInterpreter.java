//package org.middleheaven.persistance.xml;
//
//import org.middleheaven.storage.model.StorableEntityModel;
//import org.middleheaven.util.criteria.Criteria;
//import org.middleheaven.util.criteria.Criterion;
//import org.middleheaven.util.criteria.CriterionOperator;
//import org.middleheaven.util.criteria.EmptyCriterion;
//import org.middleheaven.util.criteria.FieldCriterion;
//import org.middleheaven.util.criteria.FieldValueCriterion;
//import org.middleheaven.util.criteria.FieldValueHolder;
//import org.middleheaven.util.criteria.LogicCriterion;
//import org.middleheaven.util.criteria.SingleObjectValueHolder;
//import org.middleheaven.util.criteria.entity.IdentityCriterion;
//
//public class XMLCriteriaInterpreter {
//
//	private StorableEntityModel model;
//	
//	public String interpreter(StorableEntityModel model, Criteria<?> criteria){
//		
//		this.model = model;
//		
//		String hardname = model.getEntityHardName();
//		
//		StringBuilder predicate = new StringBuilder();
//		addPredicate(predicate, criteria.constraints());
//		
//		StringBuilder builder = new StringBuilder("//").append( hardname.toLowerCase());
//		if (predicate.length()>0){
//			builder
//			.append("[")
//			.append(predicate)
//			.append("]");
//		}
//
//		return builder.toString();
//	}
//
//	private void addPredicate(StringBuilder builder, Criterion c) {
//		 if (c instanceof FieldCriterion){
//			FieldCriterion fc = (FieldCriterion)c;
//			
//			CriterionOperator op = fc.getOperator();
//			if (!op.isNegated()){
//				if ( op.equals(CriterionOperator.EQUAL)){
//					builder.append(fc.getFieldName().getName())
//					.append("=")
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'");
//				} else if ( op.equals(CriterionOperator.LESS_THAN)){
//					builder.append(fc.getFieldName().getName())
//					.append("<")
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'");
//				} else if ( op.equals(CriterionOperator.LESS_THAN_OR_EQUAL)){
//					builder.append(fc.getFieldName().getName())
//					.append("<=")
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'");
//				}else if ( op.equals(CriterionOperator.GREATER_THAN)){
//					builder.append(fc.getFieldName().getName())
//					.append(">")
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'");
//				}else if ( op.equals(CriterionOperator.GREATER_THAN_OR_EQUAL)){
//					builder.append(fc.getFieldName().getName())
//					.append(">=")
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'");
//				}else if ( op.equals(CriterionOperator.STARTS_WITH)){
//					builder.append("starts-with(")
//					.append(fc.getFieldName().getName())
//					.append(',')
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'")
//					.append(')');	
//				}else if ( op.equals(CriterionOperator.CONTAINS)){
//					builder.append("contains(")
//					.append(fc.getFieldName().getName())
//					.append(',')
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'")
//					.append(')');	
//				}else if ( op.equals(CriterionOperator.STARTS_WITH)){
//					builder.append("ends-with(")
//					.append(fc.getFieldName().getName())
//					.append(',')
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'")
//					.append(')');	
//				}
//				
//			} else { // not
//				if ( op.equals(CriterionOperator.EQUAL)){
//					builder.append(fc.getFieldName().getName())
//					.append("!=")
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'");
//				} else if ( op.equals(CriterionOperator.LESS_THAN)){
//					builder.append(fc.getFieldName().getName())
//					.append(">=")
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'");
//				} else if ( op.equals(CriterionOperator.LESS_THAN_OR_EQUAL)){
//					builder.append(fc.getFieldName().getName())
//					.append(">")
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'");
//				}else if ( op.equals(CriterionOperator.GREATER_THAN)){
//					builder.append(fc.getFieldName().getName())
//					.append("<=")
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'");
//				}else if ( op.equals(CriterionOperator.GREATER_THAN_OR_EQUAL)){
//					builder.append(fc.getFieldName().getName())
//					.append("<")
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'");
//				}else if ( op.equals(CriterionOperator.STARTS_WITH)){
//					builder.append("not(starts-with(")
//					.append(fc.getFieldName().getName())
//					.append(',')
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'")
//					.append("))");	
//				}else if ( op.equals(CriterionOperator.CONTAINS)){
//					builder.append("not(contains(")
//					.append(fc.getFieldName().getName())
//					.append(',')
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'")
//					.append("))");	
//				}else if ( op.equals(CriterionOperator.STARTS_WITH)){
//					builder.append("not(ends-with(")
//					.append(fc.getFieldName().getName())
//					.append(',')
//					.append("'")
//					.append(fc.valueHolder().getValue().toString())
//					.append("'")
//					.append("))");	
//				}
//				
//			}
//			
//		} else if (c instanceof LogicCriterion){
//			LogicCriterion lc = (LogicCriterion)c;
//			for (Criterion sc : lc.criterias()){
//				addPredicate(builder, sc);
//				builder
//				.append(' ')
//				.append(lc.getOperator().toString().toLowerCase())
//				.append(' ');
//			}
//			if (builder.length()>0){
//				builder.delete(builder.length() - (lc.getOperator().toString().length()+2), builder.length());
//			}
//		}
//	}
//	
//
//}
