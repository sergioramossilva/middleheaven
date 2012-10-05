/**
 * 
 */
package org.middleheaven.persistance;

import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.criteria.building.RelationOperator;
import org.middleheaven.persistance.model.DataSetModel;
import org.middleheaven.util.Hash;

/**
 * 
 */
public class RelatedDataSet {

	
	private String sourceAlias;
	private String targetAlias;
	private LogicConstraint relationConstraint;
	private RelationOperator relationOperator;
	private DataSetModel sourceDataSetModel;
	private DataSetModel targetDataSetModel;
	
	
	public RelatedDataSet(
			LogicConstraint relationConstraint,
			RelationOperator relationOperator) {
		super();
		this.relationConstraint = relationConstraint;
		this.relationOperator = relationOperator;
	}

	public String toString(){
		return sourceDataSetModel.getName() + " " + relationOperator.name() + " " + targetDataSetModel.getName();
	}
	public RelationOperator getRelationOperator() {
		return relationOperator;
	}


	public String getSourceAlias() {
		return sourceAlias;
	}
	public void setSourceAlias(String sourceAlias) {
		this.sourceAlias = sourceAlias;
	}
	public String getTargetAlias() {
		return targetAlias;
	}
	public void setTargetAlias(String targetAlias) {
		this.targetAlias = targetAlias;
	}
	public LogicConstraint getRelationConstraint() {
		return relationConstraint;
	}
	public void setRelationConstraint(LogicConstraint relationConstraint) {
		this.relationConstraint = relationConstraint;
	}

	/**
	 * @return
	 */
	public DataSetModel getSourceDataSetModel() {
		return this.sourceDataSetModel;
	}

	public DataSetModel getTargetDataSetModel() {
		return targetDataSetModel;
	}

	public void setTargetDataSetModel(DataSetModel targetTableModel) {
		this.targetDataSetModel = targetTableModel;
	}

	public void setSourceDataSetModel(DataSetModel sourceTableModel) {
		this.sourceDataSetModel = sourceTableModel;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode(){
		return Hash.hash(sourceDataSetModel).hash(targetDataSetModel).hashCode();
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean equals(Object other){
		return (other instanceof RelatedDataSet) && equalsOther((RelatedDataSet)other);
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(RelatedDataSet other) {
		return (this.sourceDataSetModel == null ? other.getSourceDataSetModel() == null  : this.sourceDataSetModel.getName().equals(other.getSourceDataSetModel().getName()))
				&& (this.targetDataSetModel == null ? other.getTargetDataSetModel() == null : this.targetDataSetModel.getName().equals(other.getTargetDataSetModel().getName()))
				&& this.relationOperator.equals(other.getRelationOperator());
	}
}
