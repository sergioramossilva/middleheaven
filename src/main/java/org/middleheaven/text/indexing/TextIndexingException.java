package org.middleheaven.text.indexing;

public class TextIndexingException extends RuntimeException{

	public static TextIndexingException handle (Throwable other){
		return new TextIndexingException(other);
	}
	
	private TextIndexingException(Throwable other){
		super(other);
	}
}
