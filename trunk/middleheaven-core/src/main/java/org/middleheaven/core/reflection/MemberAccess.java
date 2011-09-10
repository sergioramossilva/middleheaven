package org.middleheaven.core.reflection;

import java.lang.reflect.Modifier;

public enum MemberAccess {

	PUBLIC,
	PROTECTED,
	PRIVATE,
	PACKAGE;
	
	
	public boolean is(int modifier){
		switch (this){
		case PRIVATE:
			return Modifier.isPrivate(modifier);
		case PACKAGE:
			return !Modifier.isPrivate(modifier) && 
			!Modifier.isPublic(modifier) && 
			!Modifier.isProtected(modifier);
		case PROTECTED:
			return !Modifier.isPublic(modifier); 
		case PUBLIC:
		default:
			return !Modifier.isProtected(modifier);
		}
	}
}
