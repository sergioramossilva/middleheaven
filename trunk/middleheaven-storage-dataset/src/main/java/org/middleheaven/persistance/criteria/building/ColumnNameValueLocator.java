/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class ColumnNameValueLocator extends ColumnValueLocator {

	
	private QualifiedName name;

	public ColumnNameValueLocator (QualifiedName name){
		this.name = name;
	}

	public QualifiedName getName() {
		return name;
	}
	
	public String toString(){
		return name.toString();
	}
}
