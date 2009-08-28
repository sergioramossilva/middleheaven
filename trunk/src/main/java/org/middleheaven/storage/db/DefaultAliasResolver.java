package org.middleheaven.storage.db;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.storage.QualifiedName;

class DefaultAliasResolver implements AliasResolver {
	
	private final Map<String , Character> aliases = new HashMap<String,Character>();

	/**
	 * @see org.middleheaven.storage.db.AliasResolver#aliasFor(java.lang.String, boolean)
	 */
	public String aliasFor(String name, boolean increment){

		Character c = aliases.get(name);
		if ( c==null){
			c = new Character('a');
			aliases.put(name,c);
		} else if (increment){
			c = Character.valueOf((char)(c.charValue()+1));
			aliases.put(name,c);
		}

		return c.toString() + "_" + name;
	}
	
	/**
	 * @see org.middleheaven.storage.db.AliasResolver#aliasFor(org.middleheaven.storage.QualifiedName, java.lang.String)
	 */
	public QualifiedName aliasFor(QualifiedName name, String aliasPrefix ){
		if ( aliasPrefix ==null ){
			return name;
		}

		QualifiedName aliasName = QualifiedName.qualify(aliasPrefix, name.getName());
		aliasName.setAlias(true);

		return aliasName;
	}
}
