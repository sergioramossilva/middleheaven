package org.middleheaven.text.indexing.lucene;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.middleheaven.text.indexing.DocumentField;
import org.middleheaven.text.indexing.DocumentFieldModel;
import org.middleheaven.text.indexing.DocumentModel;
import org.middleheaven.text.indexing.IndexableDocument;
import org.middleheaven.util.collections.TransformedCollection;
import org.middleheaven.util.function.Mapper;

public class LuceneDocumentAdapter implements IndexableDocument {

	
	private Document doc;
	private DocumentModel model;


	public LuceneDocumentAdapter(Document doc, DocumentModel model) {
		this.doc = doc;
		this.model = model;
	}
	

	@Override
	public DocumentField getField(String name) {
		final Field field = doc.getField(name);
		
		return new DocumentField(){

			@Override
			public String getName() {
				return field.name();
			}

			@Override
			public String getValue() {
				return field.stringValue();
			}
			
		};
	}

	@Override
	public Iterator<DocumentField> iterator() {
		
		return TransformedCollection.transform(model.getFields(), new Mapper<DocumentField,DocumentFieldModel>(){

			@Override
			public DocumentField apply(DocumentFieldModel obj) {
				return getField(obj.getName());
			}

		}).iterator();
	
	}


	@Override
	public Serializable getDocumentModelIdentifier() {
		return model.getIdentifier();
	}
	


}
