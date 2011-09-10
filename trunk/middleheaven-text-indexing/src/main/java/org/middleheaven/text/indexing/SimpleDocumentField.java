/**
 * 
 */
package org.middleheaven.text.indexing;

class SimpleDocumentField implements DocumentField{

	private String name;
	private String value;

	public SimpleDocumentField(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

	
	
	
}