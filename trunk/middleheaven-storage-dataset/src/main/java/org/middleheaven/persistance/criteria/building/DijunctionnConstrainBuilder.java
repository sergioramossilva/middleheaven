package org.middleheaven.persistance.criteria.building;


public interface DijunctionnConstrainBuilder extends LogicConstrainBuilder {

	ColumnSelectorBuilder<ConjunctionConstrainBuilder , ComparisonOperatorBuilder<ConjunctionConstrainBuilder>> or();
	
	DataSetConstrainBuilder addDataSet(String dataSetName);
	
}
