package org.middleheaven.text.indexing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SimpleIndexableDocument implements IndexableDocument{

	private final Map<String,DocumentField> fields = new HashMap<String,DocumentField>();
	private Serializable documentModelIdentifier;

	
	public SimpleIndexableDocument(Serializable documentModelIdentifier){
		this.documentModelIdentifier = documentModelIdentifier;
	}
	
	@Override
	public Iterator<DocumentField> iterator() {
		return fields.values().iterator();
	}

	public SimpleIndexableDocument addField(String name, String value){
		fields.put(name,new SimpleDocumentField(name, value));
		return this;
	}

	@Override
	public DocumentField getField(String name) {
		return fields.get(name);
	}

	@Override
	public Serializable getDocumentModelIdentifier() {
		return documentModelIdentifier;
	}
}
