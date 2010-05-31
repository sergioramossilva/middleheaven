package org.middleheaven.text.indexing;

public class SimpleSearchHit<T> implements SearchHit<T> {

	private final T object;
	private final HitScore score;

	public SimpleSearchHit(T object , HitScore score){
		this.object = object;
		this.score = score;
	}
	
	@Override
	public T getUserObject() {
		return object;
	}

	@Override
	public HitScore getScore() {
		return score;
	}

}
