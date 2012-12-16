package org.middleheaven.quantity.time.clocks;

import org.middleheaven.quantity.time.Period;

/**
 * An utility to compute relative time intervals. 
 * 
 */
public final class StopWatch {

	/**
	 * 
	 * @return
	 */
	public static StopWatch start(){
		return start(SystemTicker.getInstance());
	}
	
	/**
	 * 
	 * @param ticker the ticker to use when measuring time intervals.
	 * @return
	 */
	public static StopWatch start(Ticker ticker){
		return new StopWatch(ticker);
	}
	
	private long mark;
	private Ticker ticker;
	
	private StopWatch(Ticker ticker){
		this.ticker = ticker;
		mark = ticker.getTick();
	}
	
	public StopWatch reset(){
		mark = ticker.getTick();
		return this;
	}
	
	/**
	 * 
	 * @return Duration in nanoseconds
	 */
	public Period mark(){
		return Period.nanoseconds(ticker.getTick() - this.mark);
	}
}
