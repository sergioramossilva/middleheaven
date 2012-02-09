package org.middleheaven.persistance.criteria.building;



public interface ComparisonOperatorBuilder<L extends LogicConstrainBuilder> extends OperatorBuilder<L> {

	L eq(Object value);
	L lt(Object value);
	L isNull();
	ComparisonOperatorBuilder<L> not();

}
