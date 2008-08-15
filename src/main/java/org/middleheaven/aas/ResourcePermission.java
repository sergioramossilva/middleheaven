package org.middleheaven.aas;

public class ResourcePermission implements Permission{

	private String resource;
	
	@Override
	public boolean implies(Permission other) {
		// TODO Auto-generated method stub
		return false;
	}

}
