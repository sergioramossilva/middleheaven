package org.middleheaven.util;

import org.middleheaven.quantity.time.Period;

public final class StopWatch {

	
	public static StopWatch start(){
		return new StopWatch();
	}
	
	long mark;
	
	private StopWatch(){
		mark = System.nanoTime();
	}
	
	public StopWatch reset(){
		mark = System.nanoTime();
		return this;
	}
	
	/**
	 * 
	 * @return Duration in nanoseconds
	 */
	public Period mark(){
		return Period.nanoseconds(System.nanoTime() - this.mark);
	}
}
