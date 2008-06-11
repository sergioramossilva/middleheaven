package org.middleheaven.util.sequence;

public class LongSequence implements Sequence<Long>{

	long current;
	public LongSequence(){
		this(0);
	}
	
	public LongSequence(long first){
		this.current = first;
	}
	
	
	@Override
	public SequenceToken<Long> next() {
		return new DefaultToken<Long>(current++);
	}

}
