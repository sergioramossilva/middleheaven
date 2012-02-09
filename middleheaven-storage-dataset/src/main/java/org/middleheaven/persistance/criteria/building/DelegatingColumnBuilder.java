/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.criteria.ResultColumnDefinition;
import org.middleheaven.persistance.criteria.ResultColumnDefinition.ResultFunction;
import org.middleheaven.persistance.model.TypeDefinition;
import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class DelegatingColumnBuilder implements ColumnBuilder {

	
	private DataSetCriteriaBuilder builder;
	private ResultColumnDefinition definition;

	public DelegatingColumnBuilder (QualifiedName name , DataSetCriteriaBuilder builder){
		this.builder = builder;
		this.definition =  new ResultColumnDefinition (name);
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnBuilder value() {
		return function(ResultFunction.VALUE);
	}
	
	private ColumnBuilder function(ResultColumnDefinition.ResultFunction function) {
		definition.setFunction(function);
		builder.criteria.addResultColumn(definition);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnBuilder as(String alias) {
		definition.setAlias(alias);
		return this;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnBuilder sum() {
		return function(ResultFunction.SUM);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnBuilder max() {
		return function(ResultFunction.MAXIMUN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnBuilder min() {
		return function(ResultFunction.MINIMUM);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnBuilder count() {
		return function(ResultFunction.COUNT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnBuilder avg() {
		return function(ResultFunction.AVERAGE);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public <T> ColumnBuilder column(TypeDefinition<?> definition) {
		return new DelegatingColumnBuilder(definition.getQualifiedName(), builder);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSetCriteriaBuilder endColumns() {
		return builder;
	}



}
