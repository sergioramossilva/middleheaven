/**
 * 
 */
package org.middleheaven.persistance.db;

import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.criteria.building.RelationOperator;
import org.middleheaven.persistance.db.metamodel.DBTableModel;

/**
 * 
 */
public class TableRelation {


	private String sourceAlias;
	private String targetAlias;
	private LogicConstraint relationConstraint;
	private RelationOperator relationOperator;
	private DBTableModel sourceTableModel;
	private DBTableModel targetTableModel;
	
	
	public TableRelation(
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
	public DBTableModel getSourceTableModel() {
		return this.sourceTableModel;
	}

	public DBTableModel getTargetTableModel() {
		return targetTableModel;
	}

	public void setTargetTableModel(DBTableModel targetTableModel) {
		this.targetTableModel = targetTableModel;
	}

	public void setSourceTableModel(DBTableModel sourceTableModel) {
		this.sourceTableModel = sourceTableModel;
	}

	
}
