package org.middleheaven.text.indexing;

import java.io.Serializable;

public interface IndexableDocument extends Iterable<DocumentField> {

	public Serializable getDocumentModelIdentifier();
	public DocumentField getField(String name);
}
