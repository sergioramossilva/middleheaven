package org.middleheaven.text.indexing;

public interface TextIndexingService {

	/**
	 * 
	 * @param indexIdentifier any object that correctly implements equals and hashcode methods.
	 * @return the text index
	 */
	public TextIndex getIndex(Object indexIdentifier) throws TextIndexingException;
	
	/**
	 * Configures a model for a document type in the index.
	 * (an index can have more than one type of document)
	 * @param indexIdentifier
	 * @param docModel
	 */
	public void configureDocument(Object indexIdentifier , DocumentModel docModel);
}
