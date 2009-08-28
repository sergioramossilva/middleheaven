package org.middleheaven.storage.db;

import org.middleheaven.storage.QualifiedName;

interface AliasResolver {

	public abstract String aliasFor(String name, boolean increment);

	public abstract QualifiedName aliasFor(QualifiedName name, String aliasPrefix);

}