package org.middleheaven.persistance.db;

import org.middleheaven.util.QualifiedName;


interface AliasResolver {

	
	public String aliasFor(String name, boolean increment);

	public QualifiedName aliasFor(QualifiedName name, String aliasPrefix);

}