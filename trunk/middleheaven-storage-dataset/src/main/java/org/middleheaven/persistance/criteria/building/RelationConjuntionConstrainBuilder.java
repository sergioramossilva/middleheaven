package org.middleheaven.persistance.criteria.building;


public interface RelationConjuntionConstrainBuilder extends LogicConstrainBuilder {

	ColumnSelectorBuilder<RelationConjuntionConstrainBuilder, RelationOperatorBuilder<RelationConjuntionConstrainBuilder> > and();
	
	DataSetConstrainBuilder toDataSet(String dataSetName);
	
	DataSetConstrainBuilder back();
}
