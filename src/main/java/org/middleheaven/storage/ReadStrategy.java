package org.middleheaven.storage;

public class ReadStrategy {

	
	private boolean isReadOnly = false;
	private boolean isFowardOnly = false;
	
	public static ReadStrategy none(){
		return new ReadStrategy(false,false);
	}
	
	public static ReadStrategy all(){
		return new ReadStrategy(true,true);
	}
	
	private ReadStrategy(boolean isReadOnly, boolean isFowardOnly) {
		this.isReadOnly = isReadOnly;
		this.isFowardOnly = isFowardOnly;
	}
	
	public boolean isReadOnly() {
		return isReadOnly;
	}
	public boolean isFowardOnly() {
		return isFowardOnly;
	}
	
	public ReadStrategy readOnly(){
		return new ReadStrategy(true,this.isFowardOnly);
	}
	
	public ReadStrategy fowardOnly(){
		return new ReadStrategy(this.isReadOnly,true);
	}
}
