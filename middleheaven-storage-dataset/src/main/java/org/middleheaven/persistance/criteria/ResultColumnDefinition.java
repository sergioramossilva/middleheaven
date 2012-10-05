/**
 * 
 */
package org.middleheaven.persistance.criteria;

import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class ResultColumnDefinition {

	public enum ResultFunction {
		VALUE,
		SUM,
		COUNT,
		AVERAGE, 
		MAXIMUN, 
		MINIMUM	
	}

	private final QualifiedName name;
	private String alias;
	private ResultFunction function = ResultFunction.VALUE;
	
	/**
	 * Constructor.
	 * @param name
	 */
	public ResultColumnDefinition(QualifiedName name) {
		this.name = name;
		this.alias = name.getQualifier() + "_" + name.getDesignation();
	}

	public String toString(){
		return name.toString() + "#" + alias + "[" + function + "]";
	}
	
	/**
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public ResultFunction getFunction() {
		return function;
	}

	public void setFunction(ResultFunction function) {
		this.function = function;
	}

	public QualifiedName getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}
	
	

}
