//package org.middleheaven.domain.criteria;
//
//import org.middleheaven.domain.model.EntityModel;
//import org.middleheaven.domain.model.ReferenceDataTypeModel;
//import org.middleheaven.domain.store.IdentityManager;
//import org.middleheaven.model.FieldModel;
//import org.middleheaven.util.classification.BooleanClassifier;
//import org.middleheaven.util.classification.LogicComposedClassifier;
//import org.middleheaven.util.criteria.Criterion;
//import org.middleheaven.util.criteria.CriterionOperator;
//import org.middleheaven.util.criteria.FieldCriterion;
//import org.middleheaven.util.criteria.FieldValueCriterion;
//import org.middleheaven.util.criteria.FieldValueHolder;
//import org.middleheaven.util.criteria.LogicCriterion;
//import org.middleheaven.util.criteria.SingleObjectValueHolder;
//import org.middleheaven.util.identity.Identity;
//
//public class CriteriaFilter<T> extends AbstractEntityCriteria<T> implements BooleanClassifier<T>{
//
//	BooleanClassifier<T> filter;
//
//	public CriteriaFilter(EntityCriteria<T> c, EntityModel model ,IdentityManager identityManager) {
//		super(c.getTargetClass());
//		filter = asFilter(c.constraints(), model,identityManager);
//	}
//
//	private CriteriaFilter (CriteriaFilter<T> other){
//		super(other);
//		this.filter = other.filter;
//	}
//
//	private BooleanClassifier<T> asFilter(Criterion c, EntityModel model,IdentityManager identityManager){
//		if ( c instanceof LogicCriterion){
//			return logicCriterionAsFilter((LogicCriterion)c, model,identityManager);
//		} else if ( c instanceof FieldValueCriterion){
//			return fieldCriterionAsFilter((FieldCriterion)c,model);
//		} else if ( c instanceof FieldJuntionCriterion){
//			return fieldJuntionCriterionAsFilter((FieldJuntionCriterion)c,model, identityManager);
//		} else if (c instanceof IdentityCriterion){
//
//			FieldValueHolder valueHolder = new SingleObjectValueHolder(
//					null, //((IdentityCriterion)c).getIdentity()
//					null //model.identityFieldModel().getDataType()
//			);
//			FieldCriterion f = new FieldValueCriterion(model.identityFieldModel().getName(), CriterionOperator.EQUAL, valueHolder);
//
//			return fieldCriterionAsFilter (f , model);
//		} else if (c instanceof EqualsOtherInstanceCriterion){
//
//			// TODO search model for identifiable groups and use the default 
//			throw new RuntimeException("Cannot convert " + c.getClass() + " to a filter");
//		} else if (c instanceof IsIdenticalToOtherInstanceCriterion){
//			IsIdenticalToOtherInstanceCriterion cri = (IsIdenticalToOtherInstanceCriterion)c;
//
//			Identity id = identityManager.getIdentityFor(cri.getInstance());
//
//			FieldValueHolder valueHolder = new SingleObjectValueHolder(id, model.identityFieldModel().getDataType());
//			FieldCriterion f = new FieldValueCriterion(model.identityFieldModel().getName(), CriterionOperator.EQUAL, valueHolder);
//
//			return fieldCriterionAsFilter (f , model);
//
//		} else {
//			throw new RuntimeException("Cannot convert " + c.getClass() + " to a filter");
//		}
//	}
//
//
//	private BooleanClassifier<T> logicCriterionAsFilter(LogicCriterion lc, EntityModel model,IdentityManager identityManager){
//		LogicComposedClassifier<T> lf =  new LogicComposedClassifier<T>(lc.getOperator());
//		for (Criterion c : lc ){
//			lf.add(asFilter(c, model,identityManager));
//		}
//		return lf;
//	}
//
//	private BooleanClassifier<T> fieldCriterionAsFilter(FieldCriterion fc, EntityModel model){
//		FieldModel fm = model.fieldModel(fc.getFieldName());
//
//		if (fm==null){
//			throw new IllegalStateException("Entity " + model.getEntityName() + " does not have a field name" + fc.getFieldName());
//		}
//		fc.valueHolder().setDataType(fm.getDataType());
//
//		final ReferenceDataTypeModel dataTypeModel = (ReferenceDataTypeModel) fm.getDataTypeModel();
//
//		fc.valueHolder().setParam("targetField", dataTypeModel.getTargetFieldName());
//		fc.valueHolder().setParam("targetFieldHardname",dataTypeModel.getTargetFieldHardName());
//		return new ReflectionFieldFilter<T>(
//				fc.getFieldName(),
//				fc.getOperator(),
//				fc.valueHolder()
//				);
//	}
//
//	@SuppressWarnings("unchecked")
//	private BooleanClassifier<T> fieldJuntionCriterionAsFilter(FieldJuntionCriterion c, EntityModel model,IdentityManager identityManager) {
//		return new JuntionFilter( c.getFieldName(), model,c.getSubCriteria(), identityManager);
//	}
//
//	@Override
//	public Boolean classify(T obj) {
//		return this.filter.classify(obj);
//	}
//
//	@Override
//	public EntityCriteria<T> duplicate() {
//		return new CriteriaFilter<T>(this);
//	}
//
//
//}
