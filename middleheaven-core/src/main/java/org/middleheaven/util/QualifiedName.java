package org.middleheaven.util;

import java.io.Serializable;

/**
 * 
 */
public class QualifiedName implements Serializable{

	private static final long serialVersionUID = 5242278443994899317L;
	
	private String qualifier;
	private String name;
	private boolean alias;
	

	public static QualifiedName qualify(String qualifier, String name){
		return new QualifiedName(qualifier,name);
	}
	
	public static QualifiedName qualify( String name){
		return new QualifiedName(null,name);
	}
	
	private QualifiedName(String qualifier, String name) {
		super();
		this.qualifier = qualifier;
		this.name = name;
	}
	
	public String getQualifier() {
		return qualifier;
	}
	public String getName() {
		return name;
	}
	
	public boolean equals(Object other) {
		return (other instanceof QualifiedName) && equals((QualifiedName) other);
	}

	public boolean equals(QualifiedName other) {
		return this.name.equals(other.name) && this.qualifier.equals(other.qualifier);
	}

	public int hashCode() {
		return Hash.hash(name).hash(qualifier).hashCode();
	}

	public void setAlias(boolean alias) {
		this.alias = alias;
	}

	public boolean isAlias() {
		return this.alias;
	}
	
	public String toString(){
		return this.qualifier + "." + this.name;
	}
}
