package org.middleheaven.persistance.criteria.building;

import org.middleheaven.util.QualifiedName;



public interface ColumnSelectorBuilder<L extends LogicConstrainBuilder , O extends OperatorBuilder<L>> {

	O column(QualifiedName name);
	O column(String dataSetName, String name);

}
