package org.middleheaven.storage;

import java.io.Serializable;

public class QualifiedName implements Serializable{

	private String qualifier;
	private String name;
	private boolean alias;
	

	public static QualifiedName qualify(String qualifier, String name){
		return new QualifiedName(qualifier,name);
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
		return other instanceof QualifiedName && equals((QualifiedName) other);
	}

	public boolean equals(QualifiedName other) {
		return this.name.equals(other.name) && this.qualifier.equals(other.qualifier);
	}

	public int hashCode() {
		return name.hashCode() ^ qualifier.hashCode();
	}

	public void setAlias(boolean alias) {
		this.alias = alias;
	}

	public boolean isAlias() {
		return this.alias;
	}
}
