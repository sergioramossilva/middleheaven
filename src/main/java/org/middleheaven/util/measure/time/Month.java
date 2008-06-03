/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.util.measure.time;

import java.util.Iterator;

/**
 * @author Sergio M.M. Taborda
 */
public class Month implements Iterable<DayOfMonth>{

 
    private int year;
    
    private int ordinal;
 
    private int daysCount;
    
    
    /**
     * @param year
     * @param ordinal
     * @param daysCount
     */
    Month(int year, int ordinal, int daysCount) {
        this.year = year;
        this.ordinal = ordinal;
        this.daysCount = daysCount;
    }
    
    Month(int year, int ordinal) {
        this.year = year;
        this.ordinal = ordinal;

    }
    
    /**
     * @return Returns the maxDays.
     */
    public int daysCount() {
        return daysCount;
    }
    /**
     * @return Returns the ordinal.
     */
    public int ordinal() {
        return ordinal;
    }
    
	@Override
	public Iterator<DayOfMonth> iterator() {
		return new DaysIterator();
	}
    
    private class DaysIterator implements Iterator<DayOfMonth>{

    	int day = 1;
		@Override
		public boolean hasNext() {
			return day <= daysCount;
		}

		@Override
		public DayOfMonth next() {
			return new DayOfMonth(day++,ordinal);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove days from month");
		}
    	
    }

	public DateHolder start() {
		return CalendarDate.date(year, ordinal, 1);
	}
	
	public DateHolder end(){
		
	}



}
