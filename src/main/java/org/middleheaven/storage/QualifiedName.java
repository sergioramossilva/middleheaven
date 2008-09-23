package org.middleheaven.storage;

import java.io.Serializable;

public class QualifiedName implements Serializable{

	private String tableName;
	private String columnName;
	
	public static QualifiedName of(String tableName, String columnName){
		return new QualifiedName(tableName,columnName);
	}
	
	private QualifiedName(String tableName, String columnName) {
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
