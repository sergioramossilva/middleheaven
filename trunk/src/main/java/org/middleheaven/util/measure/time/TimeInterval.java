/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.util.measure.time;


import org.middleheaven.util.Interval;


/**
 * @author Sergio M.M. Taborda
 */
public class TimeInterval extends Interval<TimePoint>{

    /**
     * @param start
     * @param end
     */
    protected TimeInterval(TimePoint start, TimePoint end) {
        super(new TimePointComparator());
        // reorder if necessary
        if (start.compareTo(end)>0){
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }
    }
    
    public Duration duration(){
    	return Duration.miliseconds(end.milliseconds()-start.milliseconds());
    }

    public Period pediod(){
    	return Period.period(end.milliseconds()-start.milliseconds());
    }
}
