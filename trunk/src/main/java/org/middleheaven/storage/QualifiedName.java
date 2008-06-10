package org.middleheaven.storage;

public class QualifiedName {

	
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
