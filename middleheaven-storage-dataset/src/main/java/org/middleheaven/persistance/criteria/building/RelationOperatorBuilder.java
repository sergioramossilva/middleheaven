package org.middleheaven.persistance.criteria.building;


public interface RelationOperatorBuilder<L extends LogicConstrainBuilder> extends OperatorBuilder<L> {

	L to(String string, String string2);

}
