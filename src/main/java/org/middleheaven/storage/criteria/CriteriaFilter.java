package org.middleheaven.storage.criteria;

import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.classification.LogicComposedClassifier;

public class CriteriaFilter<T> extends AbstractCriteria<T> implements BooleanClassifier<T>{

	BooleanClassifier<T> filter;
	
	public CriteriaFilter(Criteria<T> c,StorableEntityModel model) {
		super(c.getTargetClass());
		filter = asFilter(c.constraints(), model);
	}
	
	private CriteriaFilter (CriteriaFilter other){
		super(other);
		this.filter = other.filter;
	}

	private BooleanClassifier<T> asFilter(Criterion c,StorableEntityModel model){
		if ( c instanceof LogicCriterion){
			return logicCriterionAsFilter((LogicCriterion)c, model);
		} else if ( c instanceof FieldValueCriterion){
			return fieldCriterionAsFilter((FieldCriterion)c,model);
		} else if ( c instanceof FieldJuntionCriterion){
			return fieldJuntionCriterionAsFilter((FieldJuntionCriterion)c,model);
		} else {
			throw new RuntimeException("Cannot convert " + c.getClass() + " to a filter");
		}
	}
	

	private BooleanClassifier<T> logicCriterionAsFilter(LogicCriterion lc, StorableEntityModel model){
		LogicComposedClassifier<T> lf =  new LogicComposedClassifier<T>(lc.getOperator());
		for (Criterion c : lc ){
			lf.add(asFilter(c, model));
		}
		return lf;
	}
	
	private BooleanClassifier<T> fieldCriterionAsFilter(FieldCriterion fc,StorableEntityModel model){
		StorableFieldModel fm = model.fieldModel(fc.getFieldName());
		
		if (fm==null){
			throw new IllegalStateException("Entity " + model.getEntityLogicName() + " does not have a field name" + fc.getFieldName());
		}
		fc.valueHolder().setDataType(fm.getDataType());
		fc.valueHolder().setParam("targetField", fm.getParam("targetField"));
		fc.valueHolder().setParam("targetFieldHardname", fm.getParam("targetFieldHardname"));
		return new ReflectionFieldFilter<T>(
				fc.getFieldName(),
				fc.getOperator(),
				fc.valueHolder()
		);
	}
	
	private BooleanClassifier<T> fieldJuntionCriterionAsFilter(FieldJuntionCriterion c,StorableEntityModel model) {
		
	
		//return new JuntionFilter( c.getFieldName(), c.getTargetEntityModel(),c.getSubCriteria());
		return new JuntionFilter( c.getFieldName(), model,c.getSubCriteria());
	}
	
	@Override
	public Boolean classify(T obj) {
		return this.filter.classify(obj);
	}

	@Override
	public Criteria<T> duplicate() {
		return new CriteriaFilter<T>(this);
	}


}
