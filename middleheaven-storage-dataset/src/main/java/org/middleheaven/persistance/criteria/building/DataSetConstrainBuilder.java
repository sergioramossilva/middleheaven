package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.criteria.DataSetCriteria;

public interface DataSetConstrainBuilder {

	DataSetConstrainBuilder readColumns(String ... columnNames);

	DataSetConstrainBuilder rename(String originalName, String asName);

	DataSetConstrainBuilder orderBy(String ... columnNames);
	DataSetConstrainBuilder groupBy(String ... columnNames);

	DataSetCriteria end();

	ColumnSelectorBuilder<ConjunctionConstrainBuilder, ComparisonOperatorBuilder<ConjunctionConstrainBuilder>> conjunction();
	ColumnSelectorBuilder<DijunctionnConstrainBuilder, ComparisonOperatorBuilder<DijunctionnConstrainBuilder>> disjunction();

	ColumnSelectorBuilder<RelationConjuntionConstrainBuilder, RelationOperatorBuilder<RelationConjuntionConstrainBuilder>> relateBy();
}
