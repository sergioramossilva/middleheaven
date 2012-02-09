package org.middleheaven.persistance.criteria.building;


public interface ConjunctionConstrainBuilder extends LogicConstrainBuilder {


	ColumnSelectorBuilder<ConjunctionConstrainBuilder , ComparisonOperatorBuilder<ConjunctionConstrainBuilder>> and();
	
	DataSetConstrainBuilder addDataSet(String dataSetName);

}
