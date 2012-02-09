/**
 * 
 */
package org.middleheaven.persistance;

import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.criteria.building.RelationOperator;
import org.middleheaven.persistance.model.DataSetModel;

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

	
	
}
