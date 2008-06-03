package org.middleheaven.util.measure.time;

public interface DateHolder {

	/**
	 * The corresponding month day. The day ordinal is bound between 1 and 31
	 *  @return The corresponding month day. 
	 *
	 */
    public DayOfMonth dayOfMonth();
    
    /**
     * The corresponding year day. The day ordinal is bound between 1 and 7
     * @return The corresponding week day
     */
    public DayOfWeek dayOfWeek();
    
    public Month month();
    
    public Year year();
    
    
    
}
