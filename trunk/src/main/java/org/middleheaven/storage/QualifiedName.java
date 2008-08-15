package org.middleheaven.storage;

import java.io.Serializable;

public class QualifiedName implements Serializable{

	private String tableName;
	private String columnName;
	
	public QualifiedName(String tableName, String columnName) {
		super();
		this.tableName = tableName;
		this.columnName = columnName;
	}
	
	public String getTableName() {
		return tableName;
	}
	public String getColumnName() {
		return columnName;
	}
}
