package org.middleheaven.text.indexing;

public interface SearchHit<T> {

	public T getUserObject();
	public HitScore getScore();
}
