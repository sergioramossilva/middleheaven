package org.middleheaven.text.indexing;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DocumentModel {

	
	private Map<String, DocumentFieldModel> fields = new HashMap<String, DocumentFieldModel>();
	private Serializable identifier;
	
	
	public DocumentModel(Serializable identifier){
		this.identifier = identifier;
	}
	
	public void addFieldModel(DocumentFieldModel field){
		this.fields.put(field.getName(), field);
	}
	
	public DocumentFieldModel getFieldModel(String name){
		return this.fields.get(name);
	}
	
	public Collection<DocumentFieldModel> getFields(){
		return Collections.unmodifiableCollection(this.fields.values());
	}

	public Serializable getIdentifier() {
		return identifier;
	}

	
}
