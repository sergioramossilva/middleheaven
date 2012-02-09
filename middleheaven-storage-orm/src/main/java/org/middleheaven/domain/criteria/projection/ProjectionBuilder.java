package org.middleheaven.domain.criteria.projection;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.util.QualifiedName;

/**
 * Helps build a {@link Projection}.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class ProjectionBuilder {

	/**
	 * Create a projection based upon the given criteria.
	 * @param criteria the criteria to project.
	 * @return the projection Builder
	 */
	public static ProjectionBuilder project(EntityCriteria<?> criteria){
		return new ProjectionBuilder(criteria);
	}

	private final Projection projection;
	private final String entityName;

	private ProjectionBuilder(EntityCriteria<?> criteria){
		this.projection = new Projection(criteria);
		this.entityName = criteria.getTargetClass().getSimpleName();
	}
	
	/**
	 * Group the results by the given field.
	 * @param fieldName the name of the field to group
	 * @return this object
	 */
	public ProjectionBuilder groupBy(String fieldName) {
		projection.addGroupCriterion(new FieldGroupCriterion(QualifiedName.qualify(entityName, fieldName)));
		return this;
	}
	
	/**
	 * Projects the sum of the given field
	 * @param fieldName the name of the field to sum
	 *  @return this object
	 */
	public ProjectionBuilder sum(String fieldName) {
		projection.add(new SumFieldOperator(QualifiedName.qualify(entityName, fieldName)));
		return this;
	}
	
	/**
	 * Projects the count of the given field
	 * @param fieldName the name of the field to count
	 *  @return this object
	 */
	public ProjectionBuilder count(String fieldName) {
		projection.add(new CountOperator(QualifiedName.qualify(entityName, fieldName)));
		return this;
	}
	
	/**
	 * Projects the count of all instance found by the criteria.
	 * 
	 *  @return this object
	 */
	public ProjectionBuilder countAll() {
		projection.add(new CountOperator(null));
		return this;
	}
	
	/**
	 * Projects the min of the given field
	 * @param fieldName the name of the field to test
	 *  @return this object
	 */
	public ProjectionBuilder min(String fieldName) {
		projection.add(new MinFieldOperator(QualifiedName.qualify(entityName, fieldName)));
		return this;
	}
	
	/**
	 * Projects the max of the given field
	 * @param fieldName the name of the field to test
	 *  @return this object
	 */
	public ProjectionBuilder max(String fieldName) {
		projection.add(new MaxFieldOperator(QualifiedName.qualify(entityName, fieldName)));
		return this;
	}
	
	/**
	 * 
	 * @param <P> the projection result type class
	 * @param resultType the projection result type.
	 * @return the projection.
	 */
	public <P> Projection<P> as(Class<P> resultType){
		this.projection.setProjectionResultType( resultType);
		return (Projection<P>) this.projection;
	}
}
