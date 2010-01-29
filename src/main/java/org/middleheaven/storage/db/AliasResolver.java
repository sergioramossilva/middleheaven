package org.middleheaven.storage.db;

import org.middleheaven.storage.QualifiedName;

interface AliasResolver {

	
	public String aliasFor(String name, boolean increment);

	public QualifiedName aliasFor(QualifiedName name, String aliasPrefix);

}