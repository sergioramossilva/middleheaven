package org.middleheaven.text.indexing;

public class HitScore implements Comparable<HitScore> {


	public static HitScore valueOf(float score){
		return new HitScore(score);
	}
	
	private float score;
	
	private HitScore(float score){
		this.score = score;
	}
	

	public double doubleValue(){
		return score;
	}

	public float floatValue(){
		return score;
	}

	@Override
	public int compareTo(HitScore other) {
		return Float.compare(this.score, other.score);
	}


}
