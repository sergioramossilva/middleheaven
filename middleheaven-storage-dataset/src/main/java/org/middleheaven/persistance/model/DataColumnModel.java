package org.middleheaven.persistance.model;

import org.middleheaven.util.QualifiedName;


/**
 * 
 */
public interface DataColumnModel {

	
	public DataSetModel getDataSetModel();
	
	public QualifiedName getName();
	
	public int getSize();

	public int getPrecision();
	
	public boolean isNullable();
	
	public String getIndexGroup();
	
	public String getUniqueGroup();
	
	public String getPrimaryKeyGroup();
	
	public boolean isInPrimaryKeyGroup();
	
	public boolean isInIndexGroup();
	
	public boolean isInUniqueGroup();
	
	public ColumnValueType getType();

	public boolean isVersion();


}
