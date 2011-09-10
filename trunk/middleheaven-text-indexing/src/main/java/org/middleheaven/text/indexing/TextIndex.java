package org.middleheaven.text.indexing;

import org.middleheaven.util.criteria.text.TextSearchCriteria;

public interface TextIndex {

	/**
	 * Add document to this index
	 * @param document
	 */
	public void addDocument(IndexableDocument document);
	
	/**
	 * Delete document from this index
	 * @param document
	 */
	public void deleteDocument(IndexableDocument document);
	
	/**
	 * Search for documents that match the specified criteria
	 * @param criteria
	 * @return
	 */
	public SearchHits<IndexableDocument> search(TextSearchCriteria<IndexableDocument> criteria);
}
