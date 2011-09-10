package org.middleheaven.util.criteria.text;

import org.middleheaven.text.indexing.IndexableDocument;



public class TextCriteriaBuilder< D extends IndexableDocument> 
extends AbstractTextCriteriaBuilder<D, TextCriteriaBuilder<D>>{

	public static <U  extends IndexableDocument> TextCriteriaBuilder<U> search (Class<U> type){
		return new TextCriteriaBuilder<U>(type);
	}
	
	public static  TextCriteriaBuilder<IndexableDocument> search (){
		return new TextCriteriaBuilder<IndexableDocument>(IndexableDocument.class);
	}
	
	protected TextCriteriaBuilder(Class<D> type) {
		super(type);
	}

	


	
}
