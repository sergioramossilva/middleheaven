package org.middleheaven.aas;

import org.middleheaven.validation.Consistencies;

public final class NameCredential implements Credential{

	
	private final String name;

	public NameCredential(String name) {
		super();
		Consistencies.consistNotEmpty(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	
	public boolean equals(Object other){
		return other instanceof NameCredential && ((NameCredential)other).name.equals(this.name);
	}
	
	public int hashCode(){
		return name.hashCode();
	}
	
}
