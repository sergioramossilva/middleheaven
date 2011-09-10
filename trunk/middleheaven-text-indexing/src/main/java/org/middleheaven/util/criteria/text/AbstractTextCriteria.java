package org.middleheaven.util.criteria.text;

import org.middleheaven.text.indexing.IndexableDocument;
import org.middleheaven.util.criteria.AbstractCriteria;

public class AbstractTextCriteria<D extends IndexableDocument> extends AbstractCriteria<D> implements TextSearchCriteria<D> {

	private Class<D> docType;
	
	public AbstractTextCriteria(Class<D> docType){
		this.docType = docType;
	}
	
	@Override
	public Class<D> getDocumentType() {
		return docType;
	}

}
